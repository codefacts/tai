package tai.orm.entity.impl

import tai.orm.entity.DependencyInfo
import tai.orm.entity.DependencyTpl
import tai.orm.entity.EntityUtils.TableMapAndDependencyMappingInfo
import tai.orm.entity.core.Entity
import tai.orm.entity.core.Field
import tai.orm.entity.core.RelationType
import tai.orm.entity.core.columnmapping.DirectRelationMapping
import tai.orm.entity.core.columnmapping.IndirectRelationMapping
import tai.orm.entity.core.columnmapping.RelationMapping
import tai.orm.entity.core.columnmapping.VirtualRelationMapping
import java.lang.RuntimeException
import java.util.*
import java.util.function.Consumer

/**
 * Created by sohan on 4/14/2017.
 */
class TableToTableDependenyMapBuilder {
    val dependencyMap: MutableMap<String, TableDependency> =
        HashMap()
    val entityNameToEntityMap: MutableMap<String, Entity> =
        HashMap()
    val entityToFieldNameToFieldIndexMap: MutableMap<String, MutableMap<String, Field>> =
        HashMap()

    fun build(entities: Collection<Entity>): TableMapAndDependencyMappingInfo {
        entities.forEach(Consumer { entity: Entity ->
            entityNameToEntityMap[entity.name] = entity
            val relationMappings: List<RelationMapping> = entity.dbMapping.relationMappings
            for (columnIndex in relationMappings.indices) {
                val dbColumnMapping = relationMappings[columnIndex]
                try {
                    dbColumnMapping.columnType
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                when (dbColumnMapping.columnType) {
                    RelationType.DIRECT -> {
                        val mapping: DirectRelationMapping = dbColumnMapping as DirectRelationMapping
                        putInTable(
                            mapping.referencingTable,
                            entity,
                            DependencyInfo(
                                getField(entity, mapping.field),
                                mapping
                            )
                        )
                    }
                    RelationType.INDIRECT -> {
                        val mapping: IndirectRelationMapping = dbColumnMapping as IndirectRelationMapping
                        putInTable(
                            mapping.referencingTable,
                            entity,
                            DependencyInfo(
                                getField(entity, mapping.field),
                                mapping
                            )
                        )
                    }
                    RelationType.VIRTUAL -> {
                        val mapping: VirtualRelationMapping = dbColumnMapping as VirtualRelationMapping
                        putInTable(
                            mapping.referencingTable,
                            entity,
                            DependencyInfo(
                                getField(entity, mapping.field),
                                mapping
                            )
                        )
                    }
                }
            }
        })
        return TableMapAndDependencyMappingInfo(
            dependencyMap,
            entityNameToEntityMap
        )
    }

    private fun putInTable(
        referencingTable: String,
        entity: Entity,
        dependencyInfo: DependencyInfo
    ) {
        var tableDependency = dependencyMap[referencingTable]
        if (tableDependency == null) {
            dependencyMap[referencingTable] = TableDependency(
                HashMap<String, DependencyTpl>()
            ).also {
                tableDependency = it
            }
        }
        tableDependency!!.add(
            entity,
            dependencyInfo
        )
    }

    private fun getField(entity: Entity, fieldName: String): Field {
        val entityName = entity.name
        var indexMap: MutableMap<String, Field>? =
            entityToFieldNameToFieldIndexMap[entityName]
        if (indexMap == null) {
            entityToFieldNameToFieldIndexMap[entityName] = HashMap<String, Field>().also {
                indexMap = it
            }
            val fields = entity.fields
            for (field in fields) {
                Objects.requireNonNull(field, "Null in fields in entity '$entityName'")
                indexMap!![field.name] = field
            }
        }
        return indexMap!![fieldName]!!
    }
}