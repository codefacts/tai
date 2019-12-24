package tai.sql.impl

import tai.base.JsonList
import tai.base.JsonMap
import tai.criteria.ops.*
import tai.sql.*
import java.util.stream.Stream

class BaseSqlDBImpl(val coreSqlDB: CoreSqlDB, val dialect: SqlDialect) : BaseSqlDB {

    override suspend fun query(sqlQuery: SqlQuery): ResultSet {
        return dialect.executePaginated(sqlQuery)
    }

    override suspend fun queryForArrays(sqlQuery: SqlQuery): List<JsonList> {
        return query(sqlQuery).results
    }

    override suspend fun queryForObjects(sqlQuery: SqlQuery): List<JsonMap> {
        return query(sqlQuery).toJsonMaps()
    }

    override suspend fun selectInto(selectInto: SqlSelectIntoOp): UpdateResult {
        return dialect.executePaginated(selectInto)
    }

    override suspend fun insertInto(insertInto: SqlInsertIntoOp): UpdateResult {
        return dialect.executePaginated(insertInto)
    }

    override suspend fun insert(insert: SqlInsert): UpdateResult {
        return execute(insert);
    }

    override suspend fun update(updateOp: SqlUpdateOp): UpdateResult {

        return coreSqlDB.execute(
            joinExpressions(
                listOf(
                    tai.criteria.ops.update(
                        updateOp.tables.map { toCriteriaExp(it) }
                    ),
                    set(
                        updateOp.values.map { columnAndValue ->
                            eq(
                                columnAndValue.columnExpression, columnAndValue.valueExpression, isParenthesis = false
                            )
                        }
                    ),
                    if (updateOp.from.isNotEmpty())
                        from(
                            updateOp.from.map { toCriteriaExp(it) }
                        )
                    else emptyCriteriaOp,
                    where(and(updateOp.where.toList()))
                )
            )
        );
    }

    override suspend fun delete(deleteOp: SqlDeleteOp): UpdateResult {
        return coreSqlDB.execute(
            joinExpressions(
                listOf(
                    tai.criteria.ops.delete(
                        table(deleteOp.database, deleteOp.table)
                    ),
                    where(
                        and(
                            deleteOp.where.toList()
                        )
                    )
                )
            )
        );
    }

    override suspend fun execute(operation: SqlOperation): UpdateResult {
        return coreSqlDB.execute(
            toCriteriaExp(operation)
        )
    }

    override suspend fun executeAll(sqlList: Stream<SqlOperation>): List<UpdateResult> {
        return coreSqlDB.executeAll(
            sqlList.map { toCriteriaExp(it) }
        )
    }
}