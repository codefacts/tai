package tai.orm.entity.impl

import tai.orm.entity.DependencyInfo
import tai.orm.entity.DependencyTpl
import tai.orm.entity.EntitiesPreprocessor
import tai.orm.entity.core.DbMapping
import tai.orm.entity.core.Entity
import tai.orm.entity.core.RelationType
import tai.orm.entity.core.columnmapping.IndirectRelationMapping
import tai.orm.entity.core.columnmapping.RelationMapping
import tai.orm.entity.core.columnmapping.IndirectRelationMappingImpl
import java.util.*
import java.util.stream.Collectors

/**
 * Created by sohan on 3/17/2017.
 */
class EntitiesPreprocessorImpl : EntitiesPreprocessor {
    override fun process(params: EntitiesPreprocessor.Params): List<Entity> {
        return InternalEntityPreprocessorImpl(params).process()
    }

    private inner class InternalEntityPreprocessorImpl(params: EntitiesPreprocessor.Params?) {
        private val entities: Collection<Entity>
        private val entityNameToEntityMap: Map<String, Entity>
        private val tableToTableDependencyMap: Map<String, TableDependency>
        fun process(): List<Entity> {
            return entities.map { processEntity(it) }
        }

        private fun processEntity(entity: Entity): Entity {
            val mappingList = entity.dbMapping.relationMappings
                .map { dbColumnMapping: RelationMapping ->
                    processColumnMapping(
                        entity,
                        dbColumnMapping
                    )
                }

            return Entity(
                entity.name,
                entity.primaryKey,
                entity.fields,
                DbMapping(
                    entity.dbMapping.table,
                    entity.dbMapping.primaryColumn,
                    entity.dbMapping.columnMappings,
                    mappingList
                )
            )
        }

        private fun processColumnMapping(
            entity: Entity,
            dbColumnMapping: RelationMapping
        ): RelationMapping {
            when (dbColumnMapping.columnType) {
                RelationType.INDIRECT -> {
                    val mapping = dbColumnMapping as IndirectRelationMapping
                    val dependencyTplOptional =
                        checkCommonRelationalValidity(entity.dbMapping.table, mapping)
                    return dependencyTplOptional
                        .flatMap { dependencyTpl: DependencyTpl ->
                            dependencyTpl.fieldToDependencyInfoMap.values.stream()
                                .filter { dependencyInfo: DependencyInfo ->
                                    if (dependencyInfo.relationMapping.columnType === RelationType.INDIRECT) {
                                        val depMapping =
                                            dependencyInfo.relationMapping as IndirectRelationMapping
                                        if (mapping.relationTable.equals(depMapping.relationTable)) {
                                            return@filter true
                                        }
                                    }
                                    false
                                }
                                .findAny()
                                .map { dependencyInfo: DependencyInfo ->
                                    setValuesIfNecessary(
                                        dbColumnMapping,
                                        dependencyInfo.relationMapping as IndirectRelationMapping
                                    )
                                }
                        }.orElse(dbColumnMapping)
                }
            }
            return dbColumnMapping
        }

        private fun setValuesIfNecessary(
            mapping: IndirectRelationMapping,
            mappingOther: IndirectRelationMapping
        ): RelationMapping {
            return if (mapping.srcForeignColumnMappingList.isNotEmpty() && mapping.dstForeignColumnMappingList.isNotEmpty()) {
                mapping
            } else IndirectRelationMappingImpl(
                mapping.referencingTable,
                mapping.referencingEntity,
                mapping.relationTable!!,
                if (mapping.srcForeignColumnMappingList.isNotEmpty()) mapping.srcForeignColumnMappingList else mappingOther.dstForeignColumnMappingList,
                if (mapping.dstForeignColumnMappingList.isNotEmpty()) mapping.dstForeignColumnMappingList else mappingOther.srcForeignColumnMappingList,
                mapping.field,
                mapping.options
            )
        }

        private fun checkCommonRelationalValidity(
            table: String,
            mapping: RelationMapping
        ): Optional<DependencyTpl> {
            val tableDependency = tableToTableDependencyMap[table] ?: return Optional.empty()
            val tableToDependencyInfoMap =
                tableDependency.tableToDependencyInfoMap
            return Optional.of(
                tableToDependencyInfoMap[mapping.referencingTable]!!
            )
        }

        init {
            entities = params!!.entities
            entityNameToEntityMap = params.entityNameToEntityMap
            tableToTableDependencyMap = params.tableToTableDependencyMap
        }
    }
}