package tai.orm.upsert

import tai.base.JsonMap
import tai.orm.entity.DbMapping
import tai.orm.entity.Entity
import tai.orm.entity.EntityMappingHelper
import tai.orm.entity.columnmapping.DirectRelationMapping
import tai.orm.entity.columnmapping.RelationMapping
import tai.orm.upsert.UpsertUtilsInternal.Companion.handleJa
import tai.orm.upsert.UpsertUtilsInternal.Companion.handleJo
import tai.orm.upsert.UpsertUtilsInternal.Companion.toList
import tai.orm.upsert.UpsertUtilsInternal.Companion.toMap
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

        fun getRelationMappingsForUpsert(entity: Entity, helper: EntityMappingHelper): Stream<RelationMapping> {

            val fieldToRelationMappingMap = entity.dbMapping.relationMappings.asSequence().map { it.field to it }.toMap()

            return entity.fields.stream()
                .filter { it.relationship != null  && (fieldToRelationMappingMap[it.name] is DirectRelationMapping || it.relationship.options.cascadeUpsert) }
                .map { fieldToRelationMappingMap[it.name] }
        }
    }
}

class UpsertUtilsInternal {
    companion object {

        fun handleJo(value: JsonMap, joHandler: JoHandler) {
            joHandler(value)
        }

        fun handleJa(value: List<JsonMap>, joHandler: JoHandler) {
            for (i in value.indices) {
                handleJo(value[i], joHandler)
            }
        }

        fun toList(value: Any): List<JsonMap> {
            return value as List<JsonMap>
        }

        fun toMap(value: Any): JsonMap {
            return value as JsonMap
        }
    }
}

typealias JoHandler = (jsonObject: JsonMap) -> Unit

