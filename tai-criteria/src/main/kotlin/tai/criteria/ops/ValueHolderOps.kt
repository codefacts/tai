package tai.criteria.ops

import tai.base.JsonMap
import tai.criteria.ex.TaiCriteriaException
import tai.criteria.operators.*
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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

fun valueOf(value: Float): JsonMap {
    return mapOf(
        op_ to float_value_,
        arg_ to value
    );
}

fun valueOf(value: Double): JsonMap {
    return mapOf(
        op_ to double_value_,
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

fun valueOf(value: LocalTime): JsonMap {
    return mapOf(
        op_ to local_time_value_,
        arg_ to value
    );
}

fun valueOf(value: Instant): JsonMap {
    return mapOf(
        op_ to instant_,
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

fun valueOf(value: Any): JsonMap {
    return when (value) {
        is Boolean -> valueOf(value)
        is Byte -> valueOf(value)
        is Short -> valueOf(value)
        is Int -> valueOf(value)
        is Long -> valueOf(value)
        is Float -> valueOf(value)
        is Double -> valueOf(value)
        is String -> valueOf(value)
        is Date -> valueOf(value)
        is LocalDate -> valueOf(value)
        is LocalDateTime -> valueOf(value)
        else -> throw TaiCriteriaException("value of type [${value::class.java.simpleName}] is not a supported type.")
    }
}

val emptyCriteriaOp = mapOf(
    op_ to empty_
)

fun nullValue(): JsonMap {
    return mapOf(
        op_ to null_value_
    );
}

