package tai.base

import java.util.Base64.getDecoder
import java.util.*
import java.time.format.DateTimeFormatter.ISO_INSTANT
import java.time.Instant
import java.util.Objects
import java.util.Base64.getDecoder
import java.time.format.DateTimeFormatter.ISO_INSTANT


fun JsonMap.getBoolean(key: String): Boolean? {
    val any = this[key];
    return any as Boolean?;
}

fun JsonMap.getInt(key: String): Int? {
    return when (val number = this[key] as Number?) {
        null -> null
        is Int -> number
        else -> number.toInt()
    }
}

fun JsonMap.getLong(key: String): Long? {
    return when (val number = this.get(key) as Number?) {
        null -> null
        is Long -> number
        else -> number.toLong()
    }
}

fun JsonMap.getFloat(key: String): Float? {
    return when (val number = this.get(key) as Number?) {
        null -> null
        is Float -> number
        else -> number.toFloat()
    }
}

fun JsonMap.getDouble(key: String): Double? {
    return when (val number = this[key] as Number?) {
        null -> null
        is Double -> number
        else -> number.toDouble()
    }
}

fun JsonMap.getString(key: String): String? {
    val cs = this[key] as CharSequence?;
    return cs?.toString();
}

fun JsonMap.getJsonMap(key: String): JsonMap? {
    return this.get(key) as JsonMap?
}

fun JsonMap.getJsonList(key: String): JsonList? {
    return this.get(key) as JsonList?
}

fun JsonMap.getBinary(key: String): ByteArray? {
    return this[key] as ByteArray?;
}

fun JsonMap.getInstant(key: String): Instant? {
    return this[key] as Instant?;
}

fun JsonList.getString(pos: Int): String? {
    val cs = this.get(pos) as CharSequence?
    return cs?.toString()
}

fun JsonList.getInteger(pos: Int): Int? {
    val number = this.get(pos) as Number?
    return if (number == null) {
        null
    } else if (number is Int) {
        number // Avoids unnecessary unbox/box
    } else {
        number.toInt()
    }
}

fun JsonList.getLong(pos: Int): Long? {
    val number = this.get(pos) as Number?
    return if (number == null) {
        null
    } else if (number is Long) {
        number // Avoids unnecessary unbox/box
    } else {
        number.toLong()
    }
}

fun JsonList.getDouble(pos: Int): Double? {
    val number = this.get(pos) as Number?
    return if (number == null) {
        null
    } else if (number is Double) {
        number // Avoids unnecessary unbox/box
    } else {
        number.toDouble()
    }
}

fun JsonList.getFloat(pos: Int): Float? {
    val number = this.get(pos) as Number?
    return if (number == null) {
        null
    } else if (number is Float) {
        number // Avoids unnecessary unbox/box
    } else {
        number.toFloat()
    }
}

fun JsonList.getBoolean(pos: Int): Boolean? {
    return this.get(pos) as Boolean?
}

fun JsonList.getJsonObject(pos: Int): JsonMap? {
    return this.get(pos) as JsonMap?
}

fun JsonList.getJsonArray(pos: Int): JsonList? {
    return this.get(pos) as JsonList?
}

fun JsonList.getBinary(pos: Int): ByteArray? {
    return this[pos] as ByteArray?;
}

fun JsonList.getInstant(pos: Int): Instant? {
    return this[pos] as Instant?;
}