package tai.orm.validation.impl

import tai.orm.entity.Entity
import tai.orm.entity.columnmapping.*
import tai.orm.validation.ex.EntityValidationException

class EntityPreprocessor {
    fun process(entities: Collection<Entity>): Collection<Entity> {
        return entities.map { processEntity(it) }
    }

    private fun processEntity(entity: Entity): Entity {
        return entity.copy(
            dbMapping = entity.dbMapping.copy(
                relationMappings = entity.dbMapping.relationMappings.map { processRelationMapping(entity.name, it) }
            )
        )
    }

    private fun processRelationMapping(entity: String, mapping: RelationMapping): RelationMapping {
        return when (mapping) {
            is DirectRelationMapping -> (mapping as DirectRelationMappingImpl).copy(
                foreignColumnMappingList = mapping.foreignColumnMappingList.toSet().toList()
            )
            is VirtualRelationMapping -> (mapping as VirtualRelationMappingImpl).copy(
                foreignColumnMappingList = mapping.foreignColumnMappingList.toSet().toList()
            )
            is IndirectRelationMapping -> (mapping as IndirectRelationMappingImpl).copy(
                srcForeignColumnMappingList = mapping.srcForeignColumnMappingList.toSet().toList(),
                dstForeignColumnMappingList = mapping.dstForeignColumnMappingList.toSet().toList()
            )
            else -> throw EntityValidationException("Invalid mapping type '${mapping::class.java}' provided in mapping $entity.${mapping.field} -> ${mapping.referencingEntity}")
        }
    }
}