package tai.criteria.ops

import tai.base.JsonMap
import tai.criteria.operators.*

fun and(vararg jsonMaps: JsonMap): JsonMap {
    return and(jsonMaps.toList());
}

fun and(jsonMaps: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to and_,
        arg_ to jsonMaps
    );
}

fun or(vararg jsonMaps: JsonMap): JsonMap {
    return or(jsonMaps.toList());
}

fun or(jsonMaps: List<JsonMap>): JsonMap {
    return mapOf(
        op_ to or_,
        arg_ to jsonMaps
    );
}

fun not(jsonMap: JsonMap): JsonMap {
    return mapOf(
        op_ to not_,
        arg_ to jsonMap
    );
}

fun eq(arg1: JsonMap, arg2: JsonMap, isParenthesis: Boolean = true): JsonMap {
    return mapOf(
        op_ to eq_,
        arg1_ to arg1,
        arg2_ to arg2,
        is_parenthesis_ to isParenthesis
    );
}

fun neq(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return mapOf(
        op_ to neq_,
        arg1_ to arg1,
        arg2_ to arg2
    );
}

fun gt(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return mapOf(
        op_ to gt_,
        arg1_ to arg1,
        arg2_ to arg2
    );
}

fun gte(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return mapOf(
        op_ to gte_,
        arg1_ to arg1,
        arg2_ to arg2
    );
}

fun lt(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return mapOf(
        op_ to lt_,
        arg1_ to arg1,
        arg2_ to arg2
    );
}

fun lte(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return mapOf(
        op_ to lte_,
        arg1_ to arg1,
        arg2_ to arg2
    );
}

fun plus(vararg jsonMaps: JsonMap): JsonMap {
    return mapOf(
        op_ to plus_,
        arg_ to jsonMaps.toList()
    );
}

fun minus(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return mapOf(
        op_ to minus_,
        arg1_ to arg1,
        arg2_ to arg2
    );
}

fun multiply(vararg jsonMaps: JsonMap): JsonMap {
    return mapOf(
        op_ to multiply_,
        arg_ to jsonMaps.toList()
    );
}

fun divide(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return mapOf(
        op_ to divide_,
        arg1_ to arg1,
        arg2_ to arg2
    );
}

fun modulo(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return mapOf(
        op_ to modulo_,
        arg1_ to arg1,
        arg2_ to arg2
    );
}
