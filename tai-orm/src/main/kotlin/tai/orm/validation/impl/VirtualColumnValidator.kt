package tai.orm.validation.impl

import tai.orm.Utils
import tai.orm.entity.Entity
import tai.orm.entity.Field
import tai.orm.entity.ForeignColumnMapping
import tai.orm.entity.RelationType
import tai.orm.entity.columnmapping.DirectRelationMapping
import tai.orm.entity.columnmapping.VirtualRelationMapping
import tai.orm.validation.ex.EntityValidationException
import java.util.*
import java.util.function.Consumer

/**
 * Created by sohan on 4/14/2017.
 */
internal class VirtualColumnValidator(
    internalEntityValidator: InternalEntityValidator,
    entity: Entity,
    field: Field,
    mapping: VirtualRelationMapping
) {
    val internalEntityValidator: InternalEntityValidator
    val entity: Entity
    val field: Field
    val mapping: VirtualRelationMapping
    val relationship = field.relationship ?: throw EntityValidationException("No relationship present for mapping '" + mapping + "' in table '" + entity.dbMapping.table + "'")
    val ownCombinedColumns = internalEntityValidator.combinedColumns(entity)
    val otherCombinedColumns = internalEntityValidator.combinedColumns(
        internalEntityValidator.entityNameToEntityMap[mapping.referencingEntity]
            ?: throw EntityValidationException("Referencing entity '${mapping.referencingEntity}' in virtual relation ${entity.name}.${field.name} -> ${mapping.referencingEntity} does not exists")
    )

    fun validate() {
        checkAllForeignColumnExistsInOppositeEntityDbMapping()
    }

    private fun checkAllForeignColumnExistsInOppositeEntityDbMapping() {
        val oppositeEntity = relationship.entity
        mapping.foreignColumnMappingList
            .forEach(Consumer { mp: ForeignColumnMapping ->
                checkOwnColumns(mp.dstColumn, oppositeEntity, mp.srcColumn)
                checkOtherEntityColumns(oppositeEntity, mp.srcColumn, mp.dstColumn)
            })
    }

    private fun checkOwnColumns(dstColumn: String, oppositeEntity: String, srcColumn: String) {
        val existInCombinedColumns = ownCombinedColumns.contains(dstColumn)
        if (!existInCombinedColumns) {
            throw EntityValidationException(
                "Column '${dstColumn}' in mapping ${mapping.referencingTable}.${srcColumn} -> ${entity.dbMapping.table}.${dstColumn} " +
                        "does not exists for virtual relation ${entity.name}.${field.name} -> ${relationship.entity}"
            )
        }
    }

    private fun checkOtherEntityColumns(oppositeEntity: String, srcColumn: String, dstColumn: String) {
        val existInCombinedColumns = otherCombinedColumns.contains(srcColumn)
        if (!existInCombinedColumns) {
            throw EntityValidationException(
                "Column '${srcColumn}' in mapping ${mapping.referencingTable}.${srcColumn} -> ${entity.dbMapping.table}.${dstColumn} " +
                        "does not exists for virtual relation ${entity.name}.${field.name} -> ${relationship.entity}"
            )
        }
    }

    init {
        Objects.requireNonNull(internalEntityValidator)
        Objects.requireNonNull(entity)
        Objects.requireNonNull(field)
        Objects.requireNonNull(mapping)
        this.internalEntityValidator = internalEntityValidator
        this.entity = entity
        this.field = field
        this.mapping = mapping
    }
}