package tai.orm.upsert

import tai.base.JsonMap
import tai.orm.entity.DbMapping
import tai.orm.entity.Entity
import tai.orm.entity.EntityMappingHelper
import tai.orm.entity.columnmapping.DirectRelationMapping
import tai.orm.entity.columnmapping.RelationMapping
import java.util.stream.Collectors
import java.util.stream.Stream

typealias JsonMapOrList = Any

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

        fun traverseEntityMapOrList(value: JsonMapOrList, jsonHandler: (jo: JsonMap) -> Unit) {
            when (value) {
                is Map<*, *> -> {
                    jsonHandler(value as JsonMap)
                }
                is List<*> -> {
                    value.forEach {
                        jsonHandler(it as JsonMap)
                    }
                }
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

