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

        checkCorrespondingColumnMappingExistsForScalerField(field)
        checkRelationshipAndRelationMappingConsistent(field)

        if (field.relationship == null) {
            return
        }

        val relationMapping = getRelationMapping(field.name)
            ?: throw EntityValidationException("No relation mapping present for relation in field '${entity.name}.${field}' -> '${field.relationship.entity}'")

        checkFieldRelationAndReferencingEntityConsistent(field, field.relationship, relationMapping)

        checkFieldNameInBothFieldAndMappingSame(field, relationMapping)

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

    private fun checkFieldNameInBothFieldAndMappingSame(field: Field, relationMapping: RelationMapping) {
        if (field.name != relationMapping.field) {
            throw EntityValidationException("Field name '${relationMapping.field}' in ${entity.name}.${field.name} -> ${field.relationship?.entity} must be same in both field and relation mapping")
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
            throw EntityValidationException("Invalid referencing entity '${mapping.referencingEntity}' provided in relation mapping ${entity.name}.${field.name} -> ${relationship.entity}")
        }

        val refEntity = entityNameToEntityMap[mapping.referencingEntity]
            ?: throw EntityValidationException("Referencing entity in relationship '${entity.name}.${field.name}' -> '${relationship.entity}' does not exist")

        if (mapping.referencingTable != refEntity.dbMapping.table) {
            throw EntityValidationException("Invalid referencing table '${mapping.referencingTable}' provided in relation mapping ${entity.name}.${field.name} -> ${relationship.entity}")
        }

        if (relationship.name == Relationship.Name.HAS_ONE) {
            if (mapping is IndirectRelationMapping) {
                throw EntityValidationException("Relationship name '${Relationship.Name.HAS_ONE}' in ${entity.name}.${field.name} -> ${relationship.entity} can not have mapping type '${IndirectRelationMapping::class.java.simpleName}'")
            }
        }
        if (relationship.name == Relationship.Name.HAS_MANY) {
            if (mapping is DirectRelationMapping) {
                throw EntityValidationException("Relationship name '${Relationship.Name.HAS_MANY}' in ${entity.name}.${field.name} -> ${relationship.entity} can not have mapping type '${DirectRelationMapping::class.java.simpleName}'")
            }
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

    private fun checkCorrespondingColumnMappingExistsForScalerField(field: Field) {
        if (field.relationship != null) {
            return
        }
        if (getColumnMapping(field.name) == null) {
            throw EntityValidationException("Corresponding column mapping does not exists for field ${entity.name}.${field.name}")
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