package tai.criteria.ops

import tai.base.JsonMap
import tai.criteria.operators.*

fun avg(arg: JsonMap): JsonMap {
    return mapOf(
        op_ to avg_,
        arg_ to arg
    );
}

fun sum(arg: JsonMap): JsonMap {
    return mapOf(
        op_ to sum_,
        arg_ to arg
    );
}

fun min(arg: JsonMap): JsonMap {
    return mapOf(
        op_ to min_,
        arg_ to arg
    );
}

fun max(arg: JsonMap): JsonMap {
    return mapOf(
        op_ to max_,
        arg_ to arg
    );
}

fun count(arg: JsonMap): JsonMap {
    return mapOf(
        op_ to count_,
        arg_ to arg
    );
}