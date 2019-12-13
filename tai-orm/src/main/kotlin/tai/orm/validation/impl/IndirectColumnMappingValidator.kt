package tai.orm.validation.impl

import tai.orm.Utils
import tai.orm.entity.Entity
import tai.orm.entity.Field
import tai.orm.entity.ForeignColumnMapping
import tai.orm.entity.RelationType
import tai.orm.entity.columnmapping.IndirectRelationMapping
import tai.orm.validation.ex.EntityValidationException
import java.util.*
import java.util.function.Consumer

/**
 * Created by sohan on 4/14/2017.
 */
internal class IndirectColumnMappingValidator(
    internalEntityValidator: InternalEntityValidator,
    entityNameToEntityMap: Map<String, Entity>,
    entity: Entity,
    field: Field,
    mapping: IndirectRelationMapping
) {
    private val internalEntityValidator: InternalEntityValidator
    private val entityNameToEntityMap: Map<String, Entity>
    val entity: Entity
    val field: Field
    val mapping: IndirectRelationMapping
    val relationship = field.relationship ?: throw EntityValidationException("No relationship present for mapping '" + mapping + "' in table '" + entity.dbMapping.table + "'")

    val ownCombinedColumns = internalEntityValidator.combinedColumns(entity)
    val otherCombinedColumns = internalEntityValidator.combinedColumns(
        internalEntityValidator.entityNameToEntityMap[mapping.referencingEntity]
            ?: throw EntityValidationException("Referencing entity '${mapping.referencingEntity}' in virtual relation ${entity.name}.${field.name} -> ${mapping.referencingEntity} does not exists")
    )

    fun validate() {
        if (mapping.srcForeignColumnMappingList.isEmpty()) {
            throw EntityValidationException("No source foreign column mapping is provided in relation mapping ${entity.name}.${field.name} -> ${relationship.entity}")
        }
        if (mapping.dstForeignColumnMappingList.isEmpty()) {
            throw EntityValidationException("No source destination column mapping is provided in relation mapping ${entity.name}.${field.name} -> ${relationship.entity}")
        }
        checkAllOwnColumnsExists()
        checkAllOtherColumnsExists()
    }

    private fun checkAllOtherColumnsExists() {
        mapping.dstForeignColumnMappingList.forEach {
            if (!otherCombinedColumns.contains(it.dstColumn)) {
                throw EntityValidationException("Column ${it.dstColumn} in mapping ${mapping.relationTable}.${it.srcColumn} -> ${mapping.referencingTable}.${it.dstColumn} " +
                        "does not exists in entity ${mapping.referencingEntity}")
            }
        }
    }

    private fun checkAllOwnColumnsExists() {
        mapping.srcForeignColumnMappingList.forEach {
            if (!ownCombinedColumns.contains(it.dstColumn)) {
                throw EntityValidationException("Column ${it.dstColumn} in mapping ${mapping.relationTable}.${it.srcColumn} -> ${entity.dbMapping.table}.${it.dstColumn} " +
                        "does not exists in entity ${entity.name}")
            }
        }
    }

    init {
        Objects.requireNonNull(internalEntityValidator)
        Objects.requireNonNull(entityNameToEntityMap)
        Objects.requireNonNull(entity)
        Objects.requireNonNull(field)
        Objects.requireNonNull(mapping)
        this.entityNameToEntityMap = entityNameToEntityMap
        this.entity = entity
        this.field = field
        this.mapping = mapping
        this.internalEntityValidator = internalEntityValidator
    }
}