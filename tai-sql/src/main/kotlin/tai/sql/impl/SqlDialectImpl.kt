package tai.sql.impl

import tai.base.JsonMap
import tai.criteria.ops.*
import tai.sql.*

class SqlDialectImpl : SqlDialect {

    override fun toPaginatedQuery(sqlQuery: SqlQuery): JsonMap {
        val exps = listOf(select(sqlQuery.selections)) + createQueryExpressions(sqlQuery);

        if (sqlQuery.pagination == null) {
            return joinExpressions(exps)
        }
        return withOffsetLimit(exps, sqlQuery.pagination);
    }

    override fun toPaginatedQuery(sqlQuery: SqlSelectIntoOp): JsonMap {
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

    override fun toPaginatedQuery(insertInto: SqlInsertIntoOp): JsonMap {
        val exps = listOf(
            insertInto(
                table(insertInto.into.database, insertInto.into.table),
                insertInto.into.columns.map { column(it) }
            )
        ) + createQueryExpressions(insertInto);

        if (insertInto.pagination == null) {
            return joinExpressions(exps)
        }
        return withOffsetLimit(exps, insertInto.pagination)
    }
}

private fun withOffsetLimit(exps: List<JsonMap>, pagination: SqlPagination): Map<String, Any?> {
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
        groupBy(sqlQuery.groupBy.map { column(it.alias, it.column) }),
        having(
            and(
                sqlQuery.having
            )
        ),
        orderBy(
            sqlQuery.orderBy.map { order(column(it.alias, it.column), it.order) }
        )
    );
}