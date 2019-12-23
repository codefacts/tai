package tai.sql.impl

import tai.base.JsonMap
import tai.criteria.ops.*
import tai.sql.*
import java.util.stream.Stream

class SqlDBImpl(val baseSqlDB: BaseSqlDB) : SqlDB, BaseSqlDB by baseSqlDB {

    override suspend fun query(query: SqlQuery): ResultSet {
        return baseSqlDB.query(query);
    }

    override suspend fun insert(table: String, jsonObject: JsonMap): UpdateResult {
        return baseSqlDB.execute(
            SqlInsert(
                table = table, data = jsonObject
            )
        )
    }

    override suspend fun insert(table: String, sqlList: Stream<JsonMap>): List<UpdateResult> {
        return baseSqlDB.executeAll(
            sqlList.map { jo -> SqlInsert(table = table, data = jo) }
        )
    }

    override suspend fun update(table: String, data: JsonMap, where: JsonMap): UpdateResult {
        return baseSqlDB.update(
            SqlUpdateOp(
                tables = listOf(FromSpec(table = table)),
                values = data.entries.map { (column, value) ->
                    ColumnAndValue(
                        column(column), if (value != null) valueOf(value) else nullValue()
                    )
                },
                where = where.map { (key, value) ->
                    if (value != null) eq(column(key), valueOf(value)) else isNull(
                        column(key)
                    )
                }
            )
        )
    }

    override suspend fun delete(table: String, where: JsonMap): UpdateResult {
        return baseSqlDB.delete(
            SqlDeleteOp(
                table = table,
                where = where.map { (key, value) ->
                    if (value != null) eq(column(key), valueOf(value)) else isNull(
                        column(key)
                    )
                }
            )
        )
    }
}