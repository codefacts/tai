package tai.sql.impl

import tai.base.JsonMap
import tai.criteria.ops.*
import tai.sql.*

class SqlDialectImpl : SqlDialect {

    override fun toExecutePaginated(sqlQuery: SqlQuery): JsonMap {
        val exps = listOf(select(sqlQuery.selections)) + createQueryExpressions(sqlQuery);

        if (sqlQuery.pagination == null) {
            return joinExpressions(exps)
        }
        return withOffsetLimit(exps, sqlQuery.pagination);
    }

    override fun toExecutePaginated(sqlQuery: SqlSelectIntoOp): JsonMap {
        val exps = listOf(
            selectInto(
                sqlQuery.selections,
                table(sqlQuery.into.table),
                sqlQuery.into.database?.let { pathExpression(it) } ?: emptyCriteriaOp
            )
        ) + createQueryExpressions(sqlQuery);

        if (sqlQuery.pagination == null) {
            return joinExpressions(exps)
        }
        return withOffsetLimit(exps, sqlQuery.pagination)
    }

    override fun toExecutePaginated(insertInto: SqlInsertIntoOp): JsonMap {
        val exps = listOf(
            insertInto(
                table(insertInto.into.database, insertInto.into.table),
                insertInto.into.columns.map { column(it) }
            )
        ) + listOf(select(insertInto.selections)) + createQueryExpressions(insertInto);

        if (insertInto.pagination == null) {
            return joinExpressions(exps)
        }
        return withOffsetLimit(exps, insertInto.pagination)
    }
}

private fun withOffsetLimit(exps: List<JsonMap>, pagination: SqlPagination): JsonMap {
    return joinExpressions(
        exps + listOf(
            limit(valueOf(pagination.size)),
            offset(valueOf(pagination.offset))
        )
    )
}

fun createQueryExpressions(sqlQuery: QueryBase): List<JsonMap> {
    return listOf(
        from(
            sqlQuery.from.map { toCriteriaExp(it) }
        ),
        where(
            and(sqlQuery.where)
        ),
        groupBy(sqlQuery.groupBy.map { it.columnExpression }),
        having(
            and(
                sqlQuery.having
            )
        ),
        orderBy(
            sqlQuery.orderBy.map { order(it.columnExpression, it.order) }
        )
    );
}