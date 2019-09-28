package tai.criteria.operators

import tai.base.JsonMap
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

fun valueOf(value: Byte): JsonMap {
    return mapOf(
        op_ to byte_value_,
        arg_ to value
    );
}

fun valueOf(value: Short): JsonMap {
    return mapOf(
        op_ to short_value_,
        arg_ to value
    );
}

fun valueOf(value: Int): JsonMap {
    return mapOf(
        op_ to int_value_,
        arg_ to value
    );
}

fun valueOf(value: Long): JsonMap {
    return mapOf(
        op_ to long_value_,
        arg_ to value
    );
}

fun valueOf(value: Boolean): JsonMap {
    return mapOf(
        op_ to boolean_value_,
        arg_ to value
    );
}

fun valueOf(value: String): JsonMap {
    return mapOf(
        op_ to string_value_,
        arg_ to value
    );
}

fun valueOf(value: Date): JsonMap {
    return mapOf(
        op_ to date_value_,
        arg_ to value
    );
}

fun valueOf(value: LocalDate): JsonMap {
    return mapOf(
        op_ to local_date_value_,
        arg_ to value
    );
}

fun valueOf(value: LocalDateTime): JsonMap {
    return mapOf(
        op_ to local_date_time_value_,
        arg_ to value
    );
}

fun and(vararg jsonMaps: JsonMap): JsonMap {
    return mapOf(
        op_ to and_,
        arg_ to jsonMaps.toList()
    );
}

fun or(vararg jsonMaps: JsonMap): JsonMap {
    return mapOf(
        op_ to or_,
        arg_ to jsonMaps.toList()
    );
}

fun not(jsonMap: JsonMap): JsonMap {
    return mapOf(
        op_ to not_,
        arg_ to jsonMap
    );
}

fun column(column: String, alias: String? = null): JsonMap {
    return mapOf(
        op_ to column_,
        arg_ to column,
        alias_ to alias
    );
}

fun table(table: String, alias: String? = null): JsonMap {
    return mapOf(
        op_ to table_,
        arg_ to table,
        alias_ to alias
    );
}

fun asOp(exp: JsonMap, alias: String?): JsonMap {
    return mapOf(
        op_ to as_,
        arg_ to exp,
        alias_ to alias
    );
}

fun like(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return mapOf(
        op_ to like_,
        arg1_ to arg1,
        arg2_ to arg2
    );
}

fun eq(arg1: JsonMap, arg2: JsonMap): JsonMap {
    return mapOf(
        op_ to eq_,
        arg1_ to arg1,
        arg2_ to arg2
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
