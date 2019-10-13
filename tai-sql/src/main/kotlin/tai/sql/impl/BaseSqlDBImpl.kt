package tai.sql.impl

import tai.criteria.ops.*
import tai.sql.*

class BaseSqlDBImpl(val coreSqlDB: CoreSqlDB, val dialect: SqlDialect) : BaseSqlDB {

    override suspend fun query(sqlQuery: SqlQuery): ResultSet {
        return coreSqlDB.query(
            dialect.toPaginatedQuery(sqlQuery)
        );
    }

    override suspend fun selectInto(selectInto: SqlSelectIntoOp): UpdateResult {
        return coreSqlDB.execute(
            dialect.toPaginatedQuery(selectInto)
        );
    }

    override suspend fun insertInto(insertInto: SqlInsertIntoOp): UpdateResult {
        return coreSqlDB.execute(
            dialect.toPaginatedQuery(insertInto)
        )
    }

    override suspend fun insert(insert: SqlInsert): UpdateResult {
        return execute(insert);
    }

    override suspend fun update(updateOp: SqlUpdateOp): UpdateResult {

        return coreSqlDB.execute(
            joinExpressions(
                listOf(
                    tai.criteria.ops.update(
                        updateOp.tables.map { asOp(table(it.database, it.table), it.asAlias) }
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
                    where(and(updateOp.where))
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
                            deleteOp.where
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

    override suspend fun executeAll(sqlList: Collection<SqlOperation>): List<UpdateResult> {
        return coreSqlDB.executeAll(
            sqlList.map { toCriteriaExp(it) }
        )
    }
}