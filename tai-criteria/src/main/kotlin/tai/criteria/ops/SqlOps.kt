package tai.criteria.ops

import tai.base.JsonMap
import tai.base.assertOrThrow
import tai.criteria.ex.CriteriaException
import tai.criteria.operators.*

fun column(src: String?, column: String): JsonMap {
    if (src == null) {
        return column(column);
    }
    return pathExpression(src, column);
}

fun column(column: String): JsonMap {
    return pathExpression(column);
}

fun table(database: String?, table: String): JsonMap {
    if (database == null) {
        return table(table);
    }
    return pathExpression(database, table);
}

fun table(table: String): JsonMap {
    return pathExpression(table);
}

fun pathExpression(vararg parts: String): JsonMap {
    assertOrThrow(parts.isNotEmpty()) { CriteriaException("No argument provided to pathExpression") }
    return mapOf(
        op_ to path_expression_,
        arg_ to parts.toList()
    );
}

fun select(arg: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to select_,
        arg_ to arg
    );
}

fun from(arg: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to from_,
        arg_ to arg
    );
}

fun join(joiningTable: JsonMap, joinOn: JsonMap, joinType: JoinType = JoinType.JOIN): JsonMap {
    return mapOf(
        op_ to join_,
        arg1_ to joiningTable,
        arg2_ to joinOn,
        join_type_ to joinType
    );
}

fun where(arg: JsonMap): JsonMap {
    return mapOf(
        op_ to where_,
        arg_ to arg
    );
}

fun groupBy(arg: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to group_by_,
        arg_ to arg
    );
}

fun having(arg: JsonMap): JsonMap {
    return mapOf(
        op_ to having_,
        arg_ to arg
    );
}

fun orderBy(orders: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to order_by_,
        arg_ to orders
    );
}

fun order(column: JsonMap, order: Order): JsonMap {
    return mapOf(
        op_ to order_,
        arg1_ to column,
        arg2_ to order
    );
}

fun joinExpressions(vararg jsonMaps: JsonMap): JsonMap {
    return joinExpressions(jsonMaps.toList());
}

fun joinExpressions(arg: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to join_expressions_,
        arg_ to arg
    );
}

fun selectInto(select: List<JsonMap>, into: JsonMap, inOp: JsonMap = emptyCriteriaOp): JsonMap {
    return mapOf(
        op_ to select_into_,
        select_ to select,
        into_ to into,
        in_ to inOp
    );
}

fun insertInto(table: JsonMap, columns: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to insert_into_,
        table_ to table,
        columns_ to columns
    );
}

fun insert(table: JsonMap, columns: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to insert_,
        table_ to table,
        columns_ to columns
    );
}

fun update(table: JsonMap): JsonMap {
    return mapOf(
        op_ to update_,
        table_ to table
    );
}

fun delete(table: JsonMap): JsonMap {
    return mapOf(
        op_ to delete_,
        arg_ to table
    );
}

fun set(arg: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to set_,
        arg_ to arg
    );
}

fun sqlValues(arg: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to sql_values_,
        arg_ to arg
    );
}

fun limit(arg: JsonMap): JsonMap {
    return mapOf(
        op_ to limit_,
        arg_ to arg
    )
}

fun offset(arg: JsonMap): JsonMap {
    return mapOf(
        op_ to offset_,
        arg_ to arg
    )
}

fun union(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return mapOf(
        op_ to union_,
        arg1_ to arg1,
        arg2_ to arg2
    );
}

fun exists(arg: JsonMap, isNot: Boolean = false): JsonMap {
    return mapOf(
        op_ to exists_,
        arg_ to arg,
        is_not_ to isNot
    )
}

fun sqlQuery(
    select: JsonMap,
    from: JsonMap,
    where: JsonMap = emptyCriteriaOp,
    groupBy: JsonMap = emptyCriteriaOp,
    having: JsonMap = emptyCriteriaOp,
    orderBy: JsonMap = emptyCriteriaOp,
    limit: JsonMap = emptyCriteriaOp,
    offset: JsonMap = emptyCriteriaOp
): JsonMap {
    return joinExpressions(
        arg = listOf(
            select, from, where, groupBy, having, orderBy, limit, offset
        )
    )
}

