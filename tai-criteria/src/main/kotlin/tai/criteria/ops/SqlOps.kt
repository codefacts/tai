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

fun asOp(exp: JsonMap, alias: String, spaceSeparated: Boolean = false): JsonMap {
    val baseMap = mapOf(
        op_ to as_,
        arg_ to exp,
        alias_ to alias
    );
    return if (spaceSeparated) baseMap + (space_separated_ to true) else baseMap;
}

fun asSpaceSeparated(exp: JsonMap, alias: String): JsonMap {
    return asOp(exp, alias, true);
}

fun join(table1: JsonMap, table2: JsonMap, joinOn: JsonMap): JsonMap {
    return mapOf(
        op_ to join_,
        arg1_ to table1,
        arg2_ to table2,
        arg3_ to joinOn
    );
}

fun orderBy(column: JsonMap, order: Order): JsonMap {
    return mapOf(
        op_ to order_by_,
        arg_ to column,
        order_ to order
    );
}

fun sqlQuery(
    select: List<JsonMap>,
    from: List<JsonMap>,
    where: JsonMap = emptyOp,
    groupBy: List<JsonMap> = listOf(),
    having: JsonMap = emptyOp,
    orderBy: List<JsonMap> = listOf()
): JsonMap {
    return mapOf(
        op_ to sql_query_,
        select_ to select,
        from_ to from,
        where_ to where,
        group_by_ to groupBy,
        having_ to having,
        order_by_ to orderBy
    );
}

