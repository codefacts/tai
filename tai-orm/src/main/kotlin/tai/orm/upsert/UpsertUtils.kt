package tai.orm.upsert

import tai.base.JsonMap
import tai.orm.entity.DbMapping
import tai.orm.entity.columnmapping.RelationMapping
import java.util.stream.Collectors
import java.util.stream.Stream

class UpsertUtils {
    companion object {

        fun toTableAndPrimaryColumnsKey(
            table: String,
            primaryColumns: List<String>,
            values: JsonMap
        ): String {
            return table +
                    "[" +
                    primaryColumns.stream()
                        .map { column: String ->
                            "$column:" + values.getValue(
                                column
                            )
                        }
                        .collect(Collectors.joining(",")) +
                    "]"
        }

        fun isNew(entity: JsonMap, isNewKey: String): Boolean {
            return entity[isNewKey] as Boolean? ?: false
        }

        fun traverseJsonTree(value: Any, joHandler: JoHandler) {
            if (value is Map<*, *>) {
                handleJo(toMap(value), joHandler)
            } else if (value is List<*>) {
                handleJa(toList(value), joHandler)
            }
        }

        private fun handleJo(value: JsonMap, joHandler: JoHandler) {
            joHandler(value)
        }

        private fun handleJa(value: List<JsonMap>, joHandler: JoHandler) {
            for (i in value.indices) {
                handleJo(value[i], joHandler)
            }
        }

        private fun toList(value: Any): List<JsonMap> {
            return value as List<JsonMap>
        }

        private fun toMap(value: Any): JsonMap {
            return value as JsonMap
        }

    }
}

typealias JoHandler = (jsonObject: JsonMap) -> Unit

