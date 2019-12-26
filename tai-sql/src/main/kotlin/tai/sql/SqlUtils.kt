package tai.sql

import tai.base.JsonMap
import tai.criteria.ops.*
import tai.sql.impl.ResultSetImpl

fun toCriteriaExp(fromSpec: FromSpec): JsonMap {
    return joinExpressions(
        listOf(
            asOp(
                table(fromSpec.database, fromSpec.table), fromSpec.alias
            )
        ) + fromSpec.joins.map { toJoin(it) }
    )
}

fun toJoin(joinSpec: JoinSpec): JsonMap {
    return join(
        asOp(
            table(joinSpec.database, joinSpec.table), joinSpec.alias
        ),
        joinOn = and(
            joinSpec.joinRules.map { toJoinRuleExp(it) }
        )
    )
}

fun toJoinRuleExp(joinRule: JoinRule): JsonMap {
    val fromColumn = joinRule.from;
    val toColumn = joinRule.to;
    return eq(
        column(fromColumn.alias, fromColumn.column),
        column(toColumn.alias, toColumn.column)
    )
}

fun toCriteriaExp(operation: SqlOperation): JsonMap {
    return when (operation) {
        is SqlInsert -> toCritExp(operation)
        is SqlUpdate -> toCritExp(operation)
        is SqlDelete -> toCritExp(operation)
    }
}

fun toCritExp(operation: SqlDelete): JsonMap {
    return joinExpressions(
        listOf(
            delete(table(operation.database, operation.table)),
            where(
                and(
                    operation.sqlConditions.map {
                        eq(arg1 = column(it.column), arg2 = valueOf(it.value))
                    }
                )
            )
        )
    )
}

fun toCritExp(operation: SqlUpdate): JsonMap {
    return joinExpressions(
        listOf(
            update(table(operation.database, operation.table)),
            set(
                operation.data.map { (key, value) ->
                    eq(
                        column(key),
                        if (value != null) valueOf(value) else nullValue(),
                        isParenthesis = false
                    )
                }
            ),
            where(
                and(
                    operation.sqlConditions.map {
                        eq(arg1 = column(it.column), arg2 = valueOf(it.value), isParenthesis = false)
                    }
                )
            )
        )
    )
}

fun toCritExp(operation: SqlInsert): JsonMap {
    return joinExpressions(
        insert(
            table(operation.database, operation.table),
            operation.data.keys.map { column(it) }
        ),
        sqlValues(
            operation.data.values.map { it?.let { valueOf(it) } ?: nullValue() }
        )
    );
}

fun emptyResultSet(): ResultSet {
    return ResultSetImpl(
        listOf(), listOf(), listOf()
    );
}

fun createQueryExpressions(sqlQuery: QueryBase): List<JsonMap> {
    return listOf(
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
        )
    )
}

fun withOffsetLimit(exps: List<JsonMap>, pagination: SqlPagination): JsonMap {
    return joinExpressions(
        exps + listOf(
            limit(valueOf(pagination.size)),
            offset(valueOf(pagination.offset))
        )
    )
}