package tai.criteria.ops

import tai.base.JsonMap
import tai.criteria.operators.*

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

fun distinct(arg: JsonMap): JsonMap {
    return mapOf(
        op_ to distinct_,
        arg_ to arg
    );
}

fun isNull(isNot: Boolean = false): JsonMap {
    return mapOf(
        op_ to is_null_,
        is_not_ to isNot
    );
}

fun isNotNull(): JsonMap {
    return mapOf(
        op_ to is_null_,
        is_not_ to true
    );
}