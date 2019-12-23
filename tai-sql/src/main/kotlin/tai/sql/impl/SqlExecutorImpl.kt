package tai.sql.impl

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import tai.base.JsonList
import tai.criteria.SqlAndParams
import tai.sql.ResultSet
import tai.sql.SqlExecutor
import tai.sql.UpdateResult
import tai.sql.ex.TaiSqlException
import java.sql.*
import java.sql.Array
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Date
import java.util.regex.Pattern
import java.util.stream.Stream
import javax.sql.DataSource
import kotlin.streams.asSequence
import kotlin.streams.toList

class SqlExecutorImpl(val dataSource: DataSource) : SqlExecutor {

    override suspend fun query(sql: String): ResultSet {
        return doQueryII(sql, emptyList())
    }

    override suspend fun query(sql: String, params: JsonList): ResultSet {
        return doQueryII(sql, params);
    }

    override suspend fun <T> queryScalar(sql: String): T {
        return queryScalar(sql, listOf());
    }

    override suspend fun <T> queryScalar(sql: String, params: JsonList): T {
        val rs = query(sql, params);
        if (rs.rowCount != 1) {
            throw TaiSqlException("Expected exactly one result but zero or many result was returned from database");
        }
        if (rs.results[0].isEmpty()) {
            throw TaiSqlException("Expected one selection but query contains no selections");
        }
        return rs.results[0][0] as T;
    }

    override suspend fun execute(sql: String): UpdateResult {
        return execute(sql, listOf());
    }

    override suspend fun execute(sql: String, params: JsonList): UpdateResult {
        return coroutineScope {
            async {
                dataSource.connection.use { con -> doExecuteII(con, sql, params); }
            }
        }.await()
    }

    override suspend fun executeALL(sqlList: Stream<String>): List<UpdateResult> {
        return coroutineScope {
            async {
                dataSource.connection.use { con ->
                    sqlList.asSequence().asFlow().map { doExecuteII(con, it, listOf()) }.toList()
                }
            }
        }.await()
    }

    override suspend fun executeAll(sqlUpdates: Stream<SqlAndParams>): List<UpdateResult> {
        return coroutineScope {
            async {
                dataSource.connection.use { con ->
                    sqlUpdates.asSequence().asFlow().map { update -> doExecuteII(con, update.sql, update.params) }.toList()
                }
            }
        }.await()
    }

    private suspend fun doQueryII(sql: String, params: JsonList): ResultSet {
        System.out.println("SQL: $sql | PARAMS: $params")
        return coroutineScope {
            async {
                dataSource.connection.use { con ->
                    con.prepareStatement(sql).use { statement ->
                        fillStatement(statement, params);
                        statement.executeQuery().use { resultSet -> toResultSet(resultSet) }
                    }
                }
            }
        }.await()
    }

    private suspend fun doExecuteII(con: Connection, sql: String, params: JsonList): UpdateResult {
        return coroutineScope {
            async {
                con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { statement ->
                    fillStatement(statement, params);
                    val updatedCount = statement.executeUpdate();
                    val rs = statement.generatedKeys ?: return@use UpdateResultImpl(0, listOf(), listOf());
                    return@use rs.use {
                        val columnCount = statement?.metaData?.columnCount ?: 1;
                        val result = (1..columnCount).map { colNo -> convertSqlValue(rs.getObject(colNo)) }.toList();
                        return@use UpdateResultImpl(
                            updatedCount = updatedCount,
                            columns = columnNames(rs),
                            result = result
                        )
                    }
                }
            }.await()
        }
    }
}

data class DbResult(val results: List<JsonList>);

@Throws(SQLException::class)
fun toResultSet(rs: java.sql.ResultSet): ResultSet {

    val rows = mutableListOf<JsonList>()

    while (rs.next()) {
        val row = (1..rs.metaData.columnCount).map { colNo ->
            convertSqlValue(
                rs.getObject(colNo)
            )
        }
        rows.add(row)
    }

    return ResultSetImpl(
        columnNames = columnNames(rs), results = rows
    );
}

fun columnNames(rs: java.sql.ResultSet): List<String> {
    val metaData = rs.metaData
    return (1..metaData.columnCount).map { colNo -> metaData.getColumnLabel(colNo) }.toList();
}


@Throws(SQLException::class)
fun convertSqlValue(value: Any?): Any? {
    if (value == null) {
        return null
    }

    // valid json types are just returned as is
    if (value is Boolean || value is String || value is Number || value is ByteArray) {
        return value
    }

    // temporal values
    if (value is Time) {
        return value.toLocalTime()
    }

    if (value is java.sql.Date) {
        return value.toLocalDate()
    }

    if (value is Timestamp) {
        return value.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
    }

    // arrays
    if (value is Array) {
        val arr = value.array as kotlin.Array<*>?;
        try {
            if (arr != null) {
                return arr.map { obj -> convertSqlValue(obj) }
            }
        } finally {
            value.free()
        }
    }

    // fallback to String
    return value.toString()
}

@Throws(SQLException::class)
fun fillStatement(statement: PreparedStatement, params: JsonList = listOf()) {

    for (i in params.indices) {
        val value = params.get(i)
        when (value) {
            null -> statement.setObject(i + 1, null)
            is LocalTime -> statement.setObject(i + 1, Time.valueOf(value))
            is LocalDate -> statement.setObject(i + 1, java.sql.Date.valueOf(value))
            is Date -> statement.setObject(i + 1, toSqlDateTime(value))
            is Instant -> statement.setObject(i + 1, Timestamp.valueOf(value.atZone(ZoneOffset.UTC).toLocalDateTime()))
            is LocalDateTime -> statement.setObject(i + 1, Timestamp.valueOf(value))
            else -> statement.setObject(i + 1, value)
        }
    }
}

fun toSqlDateTime(value: Date): Any {
    val cal = GregorianCalendar();
    cal.time = value;
    val isDateOnly = cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0
            && cal.get(Calendar.SECOND) == 0 && cal.get(Calendar.MILLISECOND) == 0;
    if (isDateOnly) {
        return java.sql.Date.valueOf(
            Instant.ofEpochMilli(value.time).atZone(ZoneOffset.UTC).toLocalDate()
        )
    }
    return Timestamp.valueOf(
        Instant.ofEpochMilli(value.time).atZone(ZoneOffset.UTC).toLocalDateTime()
    );
}
