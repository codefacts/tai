package tai.criteria.ops

import tai.base.JsonMap
import tai.criteria.operators.*

fun like(arg1: JsonMap, arg2: JsonMap, isNot: Boolean = false): JsonMap {
    return mapOf(
        op_ to like_,
        arg1_ to arg1,
        arg2_ to arg2,
        is_not_ to isNot
    );
}

fun notLike(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return like(arg1, arg2, isNot = true);
}

fun inOp(arg1: JsonMap, arg2: List<JsonMap>, isNot: Boolean = false): JsonMap {
    return mapOf(
        op_ to in_,
        arg1_ to arg1,
        arg2_ to arg2,
        is_not_ to isNot
    );
}

fun notIn(arg1: JsonMap, arg2: List<JsonMap>): JsonMap {
    return inOp(arg1, arg2, isNot = true);
}

fun between(arg1: JsonMap, arg2: JsonMap, arg3: JsonMap, isNot: Boolean = false): JsonMap {
    return mapOf(
        op_ to between_,
        arg1_ to arg1,
        arg2_ to arg2,
        arg3_ to arg3,
        is_not_ to isNot
    );
}

fun notBetween(arg1: JsonMap, arg2: JsonMap, arg3: JsonMap): JsonMap {
    return between(arg1, arg2, arg3, isNot = true);
}