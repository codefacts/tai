package tai.orm.entity.impl

import tai.orm.Utils
import tai.orm.entity.DependencyTpl
import tai.orm.entity.EntityUtils
import tai.orm.entity.EntityValidator
import tai.orm.entity.core.*
import tai.orm.entity.core.columnmapping.*
import tai.orm.entity.ex.EntityValidationException
import tai.orm.entity.ex.InternalEntityValidatorException
import java.util.*

/**
 * Created by sohan on 4/14/2017.
 */
internal class InternalEntityValidator(params: EntityValidator.Params) {
    val entity: Entity
    val fieldNameToFieldMap: Map<String, Field>
    val fieldToColumnMappingMap: Map<String, ColumnMapping>
    val fieldToRelationMappingMap: Map<String, RelationMapping>
    val tableToTableDependencyMap: Map<String, TableDependency>
    val entityNameToEntityMap: Map<String, Entity>
    val entityToCombinedColumnsMap: EntityToCombinedColumnsMap
    fun validate() {
        validatePrimaryKey()
        for (field in entity.fields) {
            validateField(field)
        }
    }

    private fun validateField(field: Field) {
        if (Utils.not(
                fieldToColumnMappingMap.containsKey(field.name) || fieldToRelationMappingMap.containsKey(
                    field.name
                )
            )
        ) {
            throw EntityValidationException("No Mapping found for field '" + entity.name + "'.'" + field.name + "'")
        }
        if (Utils.not(fieldToRelationMappingMap.containsKey(field.name))) {
            val mapping = fieldToColumnMappingMap[field.name]
            val typeIsOk =
                field.javaType != JavaType.OBJECT && field.javaType != JavaType.ARRAY
            if (Utils.not(typeIsOk)) {
                throw EntityValidationException("Type '" + field.javaType + "' of Field '" + field.name + "' is not supported for mapping '" + ColumnMapping::class.java.simpleName + "'")
            }
            if (field.relationship != null) {
                throw EntityValidationException("Field '" + field.name + "' can not have relationship '" + field.relationship + "' with ColumnMapping '" + mapping + "'")
            }
            return
        }
        val relationMapping = fieldToRelationMappingMap[field.name]
        when (relationMapping!!.columnType) {
            RelationType.INDIRECT -> {
                IndirectColumnMappingValidator(
                    this, tableToTableDependencyMap, entityNameToEntityMap, entity,
                    field,
                    relationMapping as IndirectRelationMapping
                ).validate()
            }
            RelationType.VIRTUAL -> {
                VirtualColumnValidator(
                    this, entity,
                    field,
                    (relationMapping as VirtualRelationMapping?)!!
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

    fun columnExistsInCombinedColumns(entity: String, column: String): Boolean {
        return entityToCombinedColumnsMap.exists(entity, column)
    }

    private fun getEntityByName(entityName: String): Entity {
        return entityNameToEntityMap[entityName]
            ?: throw InternalEntityValidatorException("No entity found for entity name '$entityName'")
    }

    fun checkRelationalValidity(mapping: RelationMapping): Optional<DependencyTpl> {
        val dependencyTplOptional = getDependencyTpl(entity, mapping)
        return dependencyTplOptional.map { dependencyTpl: DependencyTpl ->
            val mappingEntityInfoIsCorrect =
                mapping.referencingTable == dependencyTpl.entity!!.dbMapping.table && mapping.referencingEntity == dependencyTpl.entity.name
            if (Utils.not(mappingEntityInfoIsCorrect)) {
                throw EntityValidationException("Referencing Entity '" + mapping.referencingEntity + "' or Table '" + mapping.referencingTable + "' does not match with actual Referencing Entity '" + dependencyTpl.entity.name + "' or Table '" + dependencyTpl.entity.dbMapping.table + "'")
            }
            dependencyTpl
        }
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

    private fun getDependencyTpl(
        entity: Entity?,
        mapping: RelationMapping
    ): Optional<DependencyTpl> {
        val tableDependency = tableToTableDependencyMap[entity!!.dbMapping.table]
            ?: return Optional.empty()
        //                throw new EntityValidationException("No TableDependency found for table '" + entity.getDbMapping().getReferencingTable() + " that has a mapping '" + mapping + "'");
        val tableToDependencyInfoMap =
            tableDependency.getTableToDependencyInfoMap()
        val dependencyTpl = tableToDependencyInfoMap[mapping.referencingTable]
        if (dependencyTpl == null && mapping.columnType == RelationType.VIRTUAL) {
            throw EntityValidationException(
                "No dependency found for referencingTable '" + mapping.referencingTable + "' in relationship '" + entity.name + "." + mapping.field + "' - '" + mapping.referencingEntity + "'"
            )
        }
        return Optional.ofNullable(dependencyTpl)
    }

    private fun validatePrimaryKey() {
        val containsKey = fieldNameToFieldMap.containsKey(entity!!.primaryKey)
        if (Utils.not(containsKey)) {
            throw EntityValidationException("Fields of Entity '" + entity.name + "' does not contains primaryKey '" + entity.primaryKey + "'")
        }
        val field = fieldNameToFieldMap[entity.primaryKey]
        val typeIsOk = (field!!.javaType != JavaType.OBJECT
                && field.javaType != JavaType.ARRAY)
        if (Utils.not(typeIsOk)) {
            throw EntityValidationException("Primary column type '" + field.javaType + "' is unsupported")
        }
        val containsDbColumn = fieldToColumnMappingMap.containsKey(field.name)
        if (Utils.not(containsDbColumn)) {
            throw EntityValidationException("No DbColumnMapping found for primary key '" + entity.primaryKey + "'")
        }
    }

    fun checkFieldTypeAndName(relationship: Relationship, field: Field) {
        if (Utils.not(
                (field.javaType == JavaType.OBJECT
                        && relationship.name == Relationship.Name.HAS_ONE) || (field.javaType == JavaType.ARRAY
                        && relationship.name == Relationship.Name.HAS_MANY)
            )
        ) {
            throw EntityValidationException(
                "javaType '" + field.javaType + "' is not valid for relationship name '" + relationship.name + "' in column '" + field.name + "'"
            )
        }
    }

    init {
        Objects.requireNonNull(params)
        entity = params.entity
        tableToTableDependencyMap = params.tableToTabledependencyMap
        entityNameToEntityMap = params.entityNameToEntityMap
        fieldNameToFieldMap = EntityUtils.toFieldNameToFieldMap(params.entity.fields)
        fieldToColumnMappingMap = EntityUtils.toFieldToColumnMappingMap(
            entity.dbMapping.columnMappings
        )
        fieldToRelationMappingMap = EntityUtils.toFieldToRelationMappingMap(
            entity.dbMapping.relationMappings
        )
        entityToCombinedColumnsMap = EntityToCombinedColumnsMap(
            entityNameToEntityMap
        )
    }
}