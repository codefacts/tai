package tai.orm.validation.impl

import tai.orm.entity.Entity
import tai.orm.entity.Field
import tai.orm.entity.RelationType
import tai.orm.entity.columnmapping.DirectRelationMapping
import tai.orm.entity.columnmapping.VirtualRelationMapping
import tai.orm.validation.ex.EntityValidationException

/**
 * Created by sohan on 4/14/2017.
 */
internal class DirectColumnValidator(
    val internalEntityValidator: InternalEntityValidator,
    val entity: Entity,
    val field: Field,
    val mapping: DirectRelationMapping
) {
    val relationship = field.relationship!!

    fun validate() {

        if (mapping.foreignColumnMappingList.isEmpty()) {
            throw EntityValidationException("No foreign column mapping is provided in relation mapping ${entity.name}.${field.name} -> ${relationship.entity}")
        }

        val otherEntityColumns = internalEntityValidator.combinedColumns(
            internalEntityValidator.entityNameToEntityMap[mapping.referencingEntity]
                ?: throw EntityValidationException("Referencing entity '${mapping.referencingEntity}' not found in relation ${entity.name}.${field.name} -> ${mapping.referencingEntity}")
        )
        mapping.foreignColumnMappingList.forEach { it ->
            if (!otherEntityColumns.contains(it.dstColumn)) {
                throw EntityValidationException("Column ${it.dstColumn} in mapping ${entity.dbMapping.table}.${it.srcColumn} -> ${mapping.referencingTable}.${it.dstColumn} does not exists in referencing entity '${mapping.referencingEntity}'")
            }
        }
    }
}