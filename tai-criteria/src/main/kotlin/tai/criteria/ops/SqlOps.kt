package tai.criteria.ops

import tai.base.JsonMap
import tai.criteria.operators.*

fun column(src: String, column: String): JsonMap {
    return mapOf(
        op_ to column_,
        arg_ to column,
        src_ to src
    );
}

fun column(column: String): JsonMap {
    return mapOf(
        op_ to column_,
        arg_ to column
    );
}

fun table(db: String, table: String): JsonMap {
    return mapOf(
        op_ to table_,
        arg_ to table,
        src_ to db
    );
}

fun table(table: String): JsonMap {
    return mapOf(
        op_ to table_,
        arg_ to table
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

fun join(table1: JsonMap, table2: JsonMap, joinOn: JsonMap, joinType: JoinType? = null): JsonMap {
    if (joinType == null) {
        return mapOf(
            op_ to join_,
            arg1_ to table1,
            arg2_ to table2,
            arg3_ to joinOn
        );
    }
    return mapOf(
        op_ to join_,
        arg1_ to table1,
        arg2_ to table2,
        arg3_ to joinOn,
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

fun criteriaJoin(arg: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to criteria_join_,
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

fun insertInto(table: JsonMap, columns: List<JsonMap>, expression: JsonMap): JsonMap {
    return mapOf(
        op_ to insert_into_,
        table_ to table,
        columns_ to columns,
        expression_ to expression
    );
}

fun update(table: JsonMap): JsonMap {
    return mapOf(
        op_ to update_,
        table_ to table
    );
}

fun delete(): JsonMap {
    return mapOf(
        op_ to delete_
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

fun sqlValueArray(arg: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to sql_value_array_,
        arg_ to arg
    );
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
    pagination: JsonMap = emptyCriteriaOp
): JsonMap {
    return criteriaJoin(
        arg = listOf(
            select, from, where, groupBy, having, orderBy, pagination
        )
    )
}

