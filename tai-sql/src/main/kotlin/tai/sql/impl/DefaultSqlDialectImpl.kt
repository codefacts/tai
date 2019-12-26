package tai.sql.impl

import tai.base.JsonMap
import tai.criteria.ops.*
import tai.criteria.withParenthesis
import tai.sql.*
import tai.sql.ex.TaiSqlException

class DefaultSqlDialectImpl(val coreSqlDB: CoreSqlDB) : SqlDialect {

    companion object {
        const val PAGINATED_QRY_ALIAS = "k"
    }

    override suspend fun executePaginated(sqlQuery: SqlQuery): ResultSet {

        return coreSqlDB.query(
            if (sqlQuery.pagination == null) {
                joinExpressions(
                    listOf(select(sqlQuery.selections)) + createQueryExpressions(sqlQuery)
                )
            } else {
                withPagination(sqlQuery, sqlQuery.pagination)
            }
        )
    }

    private fun withPagination(sqlQuery: SqlQuery, pagination: SqlPagination): JsonMap {

        if (pagination.paginationColumnSpec == null) {
            return withOffsetLimit(
                listOf(select(sqlQuery.selections)) + createQueryExpressions(sqlQuery),
                pagination
            )
        }

        if (pagination.paginationColumnSpec.alias == null) {
            throw TaiSqlException("Pagination column '${pagination.paginationColumnSpec}' without alias is not supported in sqlQuery with from '${sqlQuery.from}'")
        }

        val paginatedQry = joinExpressions(
            select(
                pagination.paginationColumnSpec.let { distinct(column(it.alias, it.column)) }
            ),
            from(
                sqlQuery.from.map { toCriteriaExp(it) }
            ),
            where(
                and(sqlQuery.where)
            ),
            groupBy(sqlQuery.groupBy),
            having(
                and(sqlQuery.having)
            ),
            orderBy(
                sqlQuery.orderBy.map { order(it.columnExpression, it.order) }
            ),
            limit(valueOf(pagination.size)),
            offset(valueOf(pagination.offset)),
            isParenthesis = true
        )

        val paginationColumnJson = pagination.paginationColumnSpec.let { column(it.alias, it.column) }

        val expressions = listOf(
            select(sqlQuery.selections),
            from(
                sqlQuery.from.map { toCriteriaExp(it) } + listOf(
                    asOp(
                        paginatedQry,
                        PAGINATED_QRY_ALIAS
                    )
                )
            ),
            where(
                and(
                    eq(
                        paginationColumnJson,
                        column(PAGINATED_QRY_ALIAS, pagination.paginationColumnSpec.column)
                    ),
                    isNotNull(paginationColumnJson)
                )
            )
        )

        return joinExpressions(expressions)
    }

    override suspend fun executePaginated(sqlQuery: SqlSelectIntoOp): UpdateResult {
        val exps = listOf(
            selectInto(
                sqlQuery.selections,
                table(sqlQuery.into.table),
                sqlQuery.into.database?.let { pathExpression(it) } ?: emptyCriteriaOp
            )
        ) + createQueryExpressions(sqlQuery);

        return coreSqlDB.execute(
            if (sqlQuery.pagination == null) {
                joinExpressions(exps)
            } else {
                withOffsetLimit(exps, sqlQuery.pagination)
            }
        )
    }

    override suspend fun executePaginated(insertInto: SqlInsertIntoOp): UpdateResult {
        val exps = listOf(
            insertInto(
                table(insertInto.into.database, insertInto.into.table),
                insertInto.into.columns.map { column(it) }
            )
        ) + listOf(select(insertInto.selections)) + createQueryExpressions(insertInto);

        return coreSqlDB.execute(
            if (insertInto.pagination == null) {
                joinExpressions(exps)
            } else {
                withOffsetLimit(exps, insertInto.pagination)
            }
        )
    }
}

