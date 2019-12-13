package tai.orm.validation.impl

import tai.orm.Utils
import tai.orm.entity.*
import tai.orm.entity.columnmapping.*
import tai.orm.validation.EntityValidator
import tai.orm.validation.ex.EntityValidationException
import java.util.*

/**
 * Created by sohan on 4/14/2017.
 */
internal class InternalEntityValidator(params: EntityValidator.Params) {
    val entity: Entity
    val fieldNameToFieldMap: Map<String, Field>
    val entityNameToEntityMap: Map<String, Entity>

    fun validate() {
        validatePrimaryKey()
        checkEveryColumnHasCorrespondingField()
        for (field in entity.fields) {
            validateField(field)
        }
    }

    private fun checkEveryColumnHasCorrespondingField() {
        val fieldNames = entity.fields.asSequence().map { it.name }.toSet()
        entity.dbMapping.columnMappings.forEach {
            if (!fieldNames.contains(it.field)) {
                throw EntityValidationException("No corresponding field found in field list for column mapping $it in entity '${entity.name}'")
            }
        }
        entity.dbMapping.relationMappings.asSequence()
            .filter { it is DirectRelationMapping }.map { it as DirectRelationMapping }
            .forEach {
                if (!fieldNames.contains(it.field)) {
                    throw EntityValidationException("No corresponding field found in field list for relation mapping '${entity.name}.${it.field}' -> '${it.referencingEntity}'")
                }
            }
    }

    private fun validateField(field: Field) {

        checkFieldAndColumnMappingConsistent(field)
        checkRelationshipAndRelationMappingConsistent(field)

        if (field.relationship == null) {
            return
        }

        val relationMapping = getRelationMapping(field.name)
            ?: throw EntityValidationException("No relation mapping present for relation in field '${entity.name}.${field}' -> '${field.relationship.entity}'")

        checkFieldRelationAndReferencingEntityConsistent(field, field.relationship, relationMapping)

        when (relationMapping.columnType) {
            RelationType.INDIRECT -> {
                IndirectColumnMappingValidator(
                    this, entityNameToEntityMap, entity,
                    field,
                    relationMapping as IndirectRelationMapping
                ).validate()
            }
            RelationType.VIRTUAL -> {
                VirtualColumnValidator(
                    this, entity,
                    field,
                    relationMapping as VirtualRelationMapping
                ).validate()
            }
            RelationType.DIRECT -> {
                DirectColumnValidator(
                    this, entity,
                    field,
                    relationMapping as DirectRelationMapping
                ).validate()
            }
        }
    }

    private fun getRelationMapping(field: String): RelationMapping? {
        return entity.dbMapping.relationMappings.find { it.field == field }
    }

    private fun checkFieldRelationAndReferencingEntityConsistent(
        field: Field, relationship: Relationship,
        mapping: RelationMapping
    ) {
        if (relationship.entity != mapping.referencingEntity) {
            throw EntityValidationException("Invalid state: relationship.entity != relationMapping.referencingEntity where relationship.entity = '${relationship.entity}' " +
                    "and relationMapping.referencingEntity = '${mapping.referencingEntity}' and field = '${entity.name}.${field.name}'")
        }

        val refEntity = entityNameToEntityMap[mapping.referencingEntity]
            ?: throw EntityValidationException("Referencing entity in relationship '${entity.name}.${field.name}' -> '${relationship.entity}' does not exist")

        if (mapping.referencingTable == refEntity.dbMapping.table) {
            throw EntityValidationException("Invalid referencing table '${mapping.referencingTable}' provided in relation mapping ${entity.name}.${field.name} -> ${relationship.entity}")
        }
    }

    private fun checkRelationshipAndRelationMappingConsistent(field: Field) {
        val relationMapping = getRelationMapping(field.name)
        val relationship = field.relationship
        val isConsistent = (relationship == null && relationMapping == null) || (relationship != null && relationMapping != null)
        if (!isConsistent) {
            throw EntityValidationException("Relationship in field '${entity.name}.${field.name}' is not consistent with relation mapping")
        }
    }

    private fun checkFieldAndColumnMappingConsistent(field: Field) {
        val isConsistent = field.relationship == null && getColumnMapping(field.name) != null
        if (!isConsistent) {
            throw EntityValidationException("Field '${entity.name}.${field.name}' is not consistent with column mapping")
        }
    }

    private fun getColumnMapping(field: String): ColumnMapping? {
        return entity.dbMapping.columnMappings.find { it.field == field }
    }

    fun combinedColumns(entity: Entity): Set<String> {
        return entity.dbMapping.columnMappings.asSequence().map { it.column }.toSet() +
                entity.dbMapping.relationMappings.asSequence().filter { it is DirectRelationMapping }
                    .map { it as DirectRelationMapping }
                    .flatMap { it.foreignColumnMappingList.asSequence().map { it.srcColumn } }.toSet()
    }

    fun isEqualDirectAndVirtual(
        directForeignColumnMappingList: List<ForeignColumnMapping>,
        virtualForeignColumnMappingList: List<ForeignColumnMapping>
    ): Boolean {
        if (directForeignColumnMappingList === virtualForeignColumnMappingList) {
            return true
        }
        if (directForeignColumnMappingList.size != virtualForeignColumnMappingList.size) {
            return false
        }
        for (i in directForeignColumnMappingList.indices) {
            val mapping1 = directForeignColumnMappingList[i]
            val mapping2 = virtualForeignColumnMappingList[i]
            val equals = mapping1
                .srcColumn ==
                    mapping2.srcColumn && mapping1.dstColumn ==
                    mapping2.dstColumn
            if (Utils.not(equals)) {
                return false
            }
        }
        return true
    }

    fun isEqualBothSide(
        srcForeignColumnMappingList: List<ForeignColumnMapping>,
        dstForeignColumnMappingList: List<ForeignColumnMapping>
    ): Boolean {
        if (srcForeignColumnMappingList === dstForeignColumnMappingList) {
            return true
        }
        if (srcForeignColumnMappingList.size != dstForeignColumnMappingList.size) {
            return false
        }
        for (i in srcForeignColumnMappingList.indices) {
            val srcMapping = srcForeignColumnMappingList[i]
            val dstMapping = dstForeignColumnMappingList[i]
            val equalBoth = isEqualBoth(srcMapping, dstMapping)
            if (Utils.not(equalBoth)) {
                return false
            }
        }
        return true
    }

    private fun isEqualBoth(srcMapping: ForeignColumnMapping, dstMapping: ForeignColumnMapping): Boolean {
        return srcMapping.srcColumn ==
                dstMapping.srcColumn && srcMapping.dstColumn ==
                dstMapping.dstColumn
    }

    private fun validatePrimaryKey() {
        val field = fieldNameToFieldMap[entity.primaryKey]
            ?: throw EntityValidationException("Primary key '${entity.name}.${entity.primaryKey}' does not exists in field list")

        val primaryColumn = getColumnMapping(field.name)?.column
            ?: throw EntityValidationException("Corresponding column mapping is not found for primary key '${entity.name}.${entity.primaryKey}'")

        if (primaryColumn != entity.dbMapping.primaryColumn) {
            throw EntityValidationException("Incorrect primary column specified in dbMapping of entity '${entity.name}'")
        }
    }

    init {
        Objects.requireNonNull(params)
        entity = params.entity
        entityNameToEntityMap = params.entityNameToEntityMap
        fieldNameToFieldMap = EntityUtils.toFieldNameToFieldMap(params.entity.fields)
    }
}