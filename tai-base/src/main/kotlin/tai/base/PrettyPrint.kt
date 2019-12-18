package tai.base

import java.lang.StringBuilder
import java.lang.UnsupportedOperationException

fun prettyPrint(obj: Any, toMapOrList: ToMapOrList): String {
    val builder = StringBuilder()
    prettyPrintII(
        any = obj, spaceCount = 2, level = 0,
        builder = builder, toMapOrList = toMapOrList
    )
    return builder.toString()
}

private fun prettyPrintII(any: Any, spaceCount: Int, level: Int, builder: StringBuilder, toMapOrList: ToMapOrList) {
    val (text, obj) = toMapOrList(any)
    appendLine(builder, spaceCount, level, text)
    if (obj == null) {
        return
    }
    when (obj) {
        is Map<*, *> -> {
            appendLine(builder, spaceCount, level, "{")
            obj.entries.forEach { (key, value) ->
                if (value == null) {
                    appendKeyValue(builder, spaceCount, level + 1, key, value)
                    return@forEach
                }
                appendLine(builder, spaceCount, level + 1, key)
                prettyPrintII(value, spaceCount, level + 2, builder, toMapOrList)
            }
            appendLine(builder, spaceCount, level, "}")
        }
        is List<*> -> {
            appendLine(builder, spaceCount, level, "[")
            obj.forEach {value ->
                if (value == null) {
                    appendLine(builder, spaceCount, level + 1, value)
                    return@forEach
                }
                prettyPrintII(value, spaceCount, level + 1, builder, toMapOrList)
            }
            appendLine(builder, spaceCount, level, "]")
        }
        else -> {
            throw UnsupportedOperationException("Object of type ${if (obj == null) null else obj::class} is not supported. Only Map or List is expected")
        }
    }
}

private fun appendLine(builder: StringBuilder, spaceCount: Int, level: Int, obj: Any?) {
    builder.append(spaces(spaceCount, level)).append(obj).append("\n")
}

private fun appendKeyValue(builder: StringBuilder, spaceCount: Int, level: Int, key: Any?, obj: Any?) {
    builder.append(spaces(spaceCount, level)).append(key).append(": ").append(obj).append("\n")
}

private fun spaces(spaceOunt: Int, level: Int): String {
    val builder = StringBuilder()
    for (i in 1..(spaceOunt * level)) {
        builder.append(" ")
    }
    return builder.toString()
}

data class TextAndCollection(val text: String, val mapOrList: Any?)

typealias ToMapOrList = (obj: Any) -> TextAndCollection