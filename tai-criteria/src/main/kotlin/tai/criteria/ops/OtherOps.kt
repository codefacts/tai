package tai.criteria.ops

import tai.base.JsonMap
import tai.criteria.operators.*

fun asOp(exp: JsonMap, alias: String?, spaceSeparated: Boolean = true): JsonMap {
    return mapOf(
        op_ to as_,
        arg_ to exp,
        alias_ to alias,
        space_separated_ to spaceSeparated
    );
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

fun isOp(column: JsonMap, type: JsonMap, isNot: Boolean = false): JsonMap {
    return mapOf(
        op_ to is_,
        arg1_ to column,
        arg2_ to type,
        is_not_ to isNot
    );
}

fun isNull(column: JsonMap): JsonMap {
    return isOp(column, nullValue());
}

fun isNotNull(column: JsonMap): JsonMap {
    return isOp(column, nullValue(), isNot = true);
}

fun star(src: String? = null): JsonMap {
    if (src == null) {
        return mapOf(
            op_ to star_
        )
    }
    return mapOf(
        op_ to star_,
        src_ to src
    )
}