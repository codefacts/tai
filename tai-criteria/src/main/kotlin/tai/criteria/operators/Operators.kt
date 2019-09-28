package tai.criteria.operators

import tai.base.JsonMap
import tai.criteria.*
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