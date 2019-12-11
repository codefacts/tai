package tai.orm.entity.impl

import tai.orm.Utils
import tai.orm.entity.DependencyInfo
import tai.orm.entity.DependencyTpl
import tai.orm.entity.core.*
import tai.orm.entity.core.columnmapping.IndirectRelationMapping
import tai.orm.entity.ex.EntityValidationException
import java.util.*
import java.util.function.Consumer

/**
 * Created by sohan on 4/14/2017.
 */
internal class IndirectColumnMappingValidator(
    internalEntityValidator: InternalEntityValidator,
    tableToTableDependencyMap: Map<String, TableDependency>,
    entityNameToEntityMap: Map<String, Entity>,
    entity: Entity,
    field: Field,
    mapping: IndirectRelationMapping
) {
    private val internalEntityValidator: InternalEntityValidator
    private val tableToTableDependencyMap: Map<String, TableDependency?>
    private val entityNameToEntityMap: Map<String, Entity>
    val entity: Entity
    val field: Field
    val mapping: IndirectRelationMapping
    fun validate() {
        checkFieldType()
        val relationship = field.relationship ?: throw EntityValidationException("No relationship present for mapping '" + mapping + "' in table '" + entity.dbMapping.table + "'")
        internalEntityValidator.checkFieldTypeAndName(relationship, field)
        if (Utils.not(tableToTableDependencyMap.containsKey(entity.dbMapping.table))) {
            val referencingEntity =
                entityNameToEntityMap[mapping.referencingEntity]
            checkRelationshipToReferencingEntity(referencingEntity)
            checkAllSrcColumnsExistsInCombinedColumns(entity.name, mapping.srcForeignColumnMappingList)
            checkAllSrcColumnsExistsInCombinedColumns(
                referencingEntity!!.name,
                mapping.dstForeignColumnMappingList
            )
            return
        }
        val dependencyTplOptional = internalEntityValidator.checkRelationalValidity(mapping)
        val info =
            dependencyTplOptional.flatMap { dependencyTpl: DependencyTpl ->
                dependencyTpl.getFieldToDependencyInfoMap()!!.values.stream()
                    .filter { dependencyInfo: DependencyInfo ->
                        findDependencyInfoInOppositeSide(
                            dependencyInfo
                        )
                    }
                    .findAny()
            }
        if (info.isPresent) {
            val dependencyInfo = info.get()
            checkMappingValidity(
                mapping,
                dependencyInfo.relationMapping as IndirectRelationMapping,
                dependencyTplOptional.get(),
                dependencyInfo
            )
        } else {
            checkAllSrcColumnsExistsInCombinedColumns(
                entity.name, mapping.srcForeignColumnMappingList
            )
            checkAllSrcColumnsExistsInCombinedColumns(
                entityNameToEntityMap[mapping.referencingEntity]!!.name,
                mapping.dstForeignColumnMappingList
            )
        }
        info.ifPresent { dependencyInfo: DependencyInfo ->
            val relationshipOther =
                dependencyInfo.field.relationship ?: throw EntityValidationException("No relationship present for mapping '" + mapping + "' in table '" + entity.dbMapping.table + "'")
            if (Utils.not(
                    (relationship.type == Relationship.Type.ONE_TO_ONE
                            && relationshipOther.type == Relationship.Type.ONE_TO_ONE)
                            || (relationship.type == Relationship.Type.ONE_TO_MANY
                            && relationshipOther.type == Relationship.Type.MANY_TO_ONE)
                            || (relationship.type == Relationship.Type.MANY_TO_ONE
                            && relationshipOther.type == Relationship.Type.ONE_TO_MANY)
                            || (relationship.type == Relationship.Type.MANY_TO_MANY
                            && relationshipOther.type == Relationship.Type.MANY_TO_MANY)
                )
            ) {
                throw EntityValidationException(
                    "Relationship type '" + relationship.type + "' is invalid for mapping type '" + mapping.columnType + "'"
                )
            }
        }
    }

    private fun findDependencyInfoInOppositeSide(dependencyInfo: DependencyInfo): Boolean {
        if (dependencyInfo.relationMapping.columnType == RelationType.INDIRECT) {
            val depMapping = dependencyInfo.relationMapping as IndirectRelationMapping
            if (mapping.relationTable == depMapping.relationTable) {
                return true
            }
        }
        return false
    }

    private fun checkRelationshipToReferencingEntity(referencingEntity: Entity?) {
        if (referencingEntity == null) {
            throw EntityValidationException(
                "No entity found for referenceEntity '" + mapping.referencingEntity + "' in mapping '" + mapping + "' in relationship '" + entity.name + "' <-> '" + mapping.referencingEntity + "'"
            )
        }
        if (Utils.not(
                mapping.referencingEntity ==
                        referencingEntity.name && mapping.referencingTable ==
                        referencingEntity.dbMapping.table
            )
        ) {
            throw EntityValidationException("Referencing Entity '" + mapping.referencingEntity + "' or Table '" + mapping.referencingTable + "' does not match with actual Referencing Entity '" + referencingEntity.name + "' or Table '" + referencingEntity.dbMapping.table + "'")
        }
    }

    private fun checkFieldType() {
        val isFieldTypeOk =
            field.javaType == JavaType.OBJECT || field.javaType == JavaType.ARRAY
        if (Utils.not(isFieldTypeOk)) {
            throw EntityValidationException(
                "Field '" + field.name + "' has an invalid type '" + field.javaType + "' for dbColumnMappingType '"
                        + mapping.columnType + "'"
            )
        }
    }

    private fun checkMappingValidity(
        mapping: IndirectRelationMapping,
        depMapping: IndirectRelationMapping,
        dependencyTpl: DependencyTpl,
        dependencyInfo: DependencyInfo
    ) {
        val srcForeignColumnMappingList =
            mapping.srcForeignColumnMappingList
        val dstForeignColumnMappingList =
            mapping.dstForeignColumnMappingList
        val srcForeignColumnMappingList1 =
            depMapping.srcForeignColumnMappingList
        val dstForeignColumnMappingList1 =
            depMapping.dstForeignColumnMappingList
        if (srcForeignColumnMappingList.size > 0 && dstForeignColumnMappingList1.size > 0 && Utils.not(
                internalEntityValidator.isEqualBothSide(srcForeignColumnMappingList, dstForeignColumnMappingList1)
            )
        ) {
            throw EntityValidationException("srcForeignColumnMappingList and dstForeignColumnMappingList does not match in relationship '" + entity.name + "." + field.name + "' <-> '" + dependencyTpl.entity!!.name + "." + dependencyInfo.field.name + "'")
        }
        if (srcForeignColumnMappingList1.size > 0 && dstForeignColumnMappingList.size > 0 && Utils.not(
                internalEntityValidator.isEqualBothSide(srcForeignColumnMappingList1, dstForeignColumnMappingList)
            )
        ) {
            throw EntityValidationException("srcForeignColumnMappingList and dstForeignColumnMappingList does not match in relationship '" + entity.name + "." + field.name + "' <-> '" + dependencyTpl.entity!!.name + "." + dependencyInfo.field.name + "'")
        }
        if (srcForeignColumnMappingList.isEmpty() && dstForeignColumnMappingList1.isEmpty()) {
            throw EntityValidationException(
                "Both srcForeignColumnMappingList and dstForeignColumnMappingList is empty in relationship '" + entity.name + "." + field.name + "' <-> '" + dependencyTpl.entity!!.name + "." + dependencyInfo.field.name + "'"
            )
        }
        if (dstForeignColumnMappingList.isEmpty() && srcForeignColumnMappingList1.isEmpty()) {
            throw EntityValidationException(
                "Both srcForeignColumnMappingList and dstForeignColumnMappingList is empty in relationship '" + entity.name + "." + field.name + "' <-> '" + dependencyTpl.entity!!.name + "." + dependencyInfo.field.name + "'"
            )
        }
        checkAllSrcColumnsExistsInCombinedColumns(
            entity.name,
            if (srcForeignColumnMappingList.size > 0) srcForeignColumnMappingList else dstForeignColumnMappingList1
        )
        checkAllSrcColumnsExistsInCombinedColumns(
            dependencyTpl.entity!!.name,
            if (dstForeignColumnMappingList.size > 0) dstForeignColumnMappingList else srcForeignColumnMappingList1
        )
    }

    private fun checkAllSrcColumnsExistsInCombinedColumns(
        entity: String,
        foreignColumnMappings: List<ForeignColumnMapping>
    ) {
        foreignColumnMappings.forEach(Consumer { foreignColumnMapping: ForeignColumnMapping ->
            val existsInCombinedColumns =
                internalEntityValidator.columnExistsInCombinedColumns(entity, foreignColumnMapping.srcColumn)
            if (Utils.not(existsInCombinedColumns)) {
                throw EntityValidationException(
                    "Mapping column '" + foreignColumnMapping.srcColumn + "' does not exists in table column list in table '" + entityNameToEntityMap[entity]!!.dbMapping.table + "'"
                )
            }
        })
    }

    init {
        Objects.requireNonNull(internalEntityValidator)
        Objects.requireNonNull(tableToTableDependencyMap)
        Objects.requireNonNull(entityNameToEntityMap)
        Objects.requireNonNull(entity)
        Objects.requireNonNull(field)
        Objects.requireNonNull(mapping)
        this.tableToTableDependencyMap = tableToTableDependencyMap
        this.entityNameToEntityMap = entityNameToEntityMap
        this.entity = entity
        this.field = field
        this.mapping = mapping
        this.internalEntityValidator = internalEntityValidator
    }
}