package tai.orm.entity

import tai.orm.entity.core.Entity
import tai.orm.entity.core.Field
import tai.orm.entity.core.columnmapping.ColumnMapping
import tai.orm.entity.core.columnmapping.RelationMapping
import tai.orm.entity.impl.*
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Created by Jango on 2017-01-21.
 */
interface EntityUtils {
    class TableMapAndDependencyMappingInfo(
        val tableToTableDependencyMap: Map<String, TableDependency>,
        val entityNameToEntityMap: Map<String, Entity>
    )

    companion object {

        fun toEntityNameToEntityMap(entities: Collection<Entity>): Map<String, Entity> {
            return entities.asSequence().map { it.name to it }.toMap()
        }

        fun toTableToEntityMap(entities: Collection<Entity>): Map<String, Entity> {
            return entities.asSequence().map { it.dbMapping.table to it }.toMap()
        }

        fun toTableToTableDependencyMap(entities: Collection<Entity>): TableMapAndDependencyMappingInfo {
            return TableToTableDependenyMapBuilder().build(entities)
        }

        fun toFieldNameToFieldMap(fields: Array<Field>): Map<String, Field> {
            return fields.asSequence().map { field -> field.name to field }.toMap()
        }

        fun toFieldToColumnMappingMap(dbColumnMappings: Array<ColumnMapping>): Map<String, ColumnMapping> {
            return dbColumnMappings.asSequence().map { it.field to it }.toMap()
        }

        fun toFieldToRelationMappingMap(relationMappings: Array<RelationMapping>): Map<String, RelationMapping> {
            return relationMappings.asSequence().map { it.field to it }.toMap()
        }

        fun toColumnNameToColumnMapingMap(columnMappings: Array<ColumnMapping>): Map<String, ColumnMapping> {
            return columnMappings.asSequence().map { it.column to it }.toMap()
        }

        fun validateAndPreProcess(entities: Collection<Entity>): Collection<Entity> {
            val tableToTableDependencyMap: Map<String, TableDependency> =
                TableToTableDependenyMapBuilder()
                    .build(entities).tableToTableDependencyMap
            val entityMap =
                toEntityNameToEntityMap(entities)
            EntitiesValidatorImpl(
                EntityValidatorImpl()
            ).validate(
                EntitiesValidator.Params(
                    entities = entities,
                    tableToTableDependencyMap = tableToTableDependencyMap,
                    entityNameToEntityMap = entityMap
                )
            )
            return EntitiesPreprocessorImpl().process(
                EntitiesPreprocessor.Params(
                    entityNameToEntityMap = entityMap,
                    entities = entities,
                    tableToTableDependencyMap = tableToTableDependencyMap
                )
            )
        }
    }
}