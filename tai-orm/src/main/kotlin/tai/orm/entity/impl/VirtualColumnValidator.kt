package tai.orm.entity.impl

import tai.orm.Utils
import tai.orm.entity.DependencyInfo
import tai.orm.entity.DependencyTpl
import tai.orm.entity.core.*
import tai.orm.entity.core.columnmapping.DirectRelationMapping
import tai.orm.entity.core.columnmapping.VirtualRelationMapping
import tai.orm.entity.ex.EntityValidationException
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
    fun validate() {
        checkFieldType()
        val relationship = field.relationship ?: throw EntityValidationException("No relationship present for mapping '" + mapping + "' in table '" + entity.dbMapping.table + "'")
        internalEntityValidator.checkFieldTypeAndName(relationship, field)
        checkRelationshipType(relationship)
        val dependencyTplOptional = internalEntityValidator.checkRelationalValidity(mapping)
        dependencyTplOptional
            .flatMap { dependencyTpl: DependencyTpl ->
                dependencyTpl.getFieldToDependencyInfoMap()!!.values.stream()
                    .filter { dependencyInfo: DependencyInfo ->
                        findDependencyInfoInOppositeSide(
                            dependencyInfo
                        )
                    }
                    .findAny()
            } //            .orElseThrow(() -> new EntityValidationException("No Mapping found in the opposite side for mapping '" + mapping + "' in relationship '" + entity.getName() + "." + column.getName() + "' <- '" + dependencyTplOptional.map(dependencyTpl -> dependencyTpl.getEntity().getName()).orElse("") + "'"));
            .ifPresent { dependencyInfo: DependencyInfo ->
                val dependencyTpl = dependencyTplOptional.get()
                val relationshipOther = dependencyInfo.field.relationship ?: throw EntityValidationException("No relationship present for mapping '" + mapping + "' in table '" + dependencyTpl.entity!!.dbMapping.table + "'")
                checkRelationshipAndJavaType(dependencyInfo, dependencyTpl, relationship, relationshipOther)
            }
        checkAllForeignColumnExistsInOppositeEntityDbMapping()
    }

    private fun findDependencyInfoInOppositeSide(dependencyInfo: DependencyInfo): Boolean {
        if (dependencyInfo.relationMapping.columnType == RelationType.DIRECT) {
            val depMapping = dependencyInfo.relationMapping as DirectRelationMapping
            //                        if (mapping.getForeignColumnMappingList().size() != depMapping.getForeignColumnMappingList().size()) {
//                            throw new EntityValidationException(
//                                "mapping.getForeignColumnMappingList().size() != depMapping.getForeignColumnMappingList().size() in relationship '" + entity.getName() + "." + column.getName() + "' -> '" + dependencyTpl.getEntity().getName() + "'"
//                            );
//                        }
            return internalEntityValidator.isEqualDirectAndVirtual(
                depMapping.foreignColumnMappingList, mapping.foreignColumnMappingList
            )
        }
        return false
    }

    private fun checkRelationshipType(relationship: Relationship) {
        if (Utils.not(relationship.type == Relationship.Type.ONE_TO_ONE || relationship.type == Relationship.Type.ONE_TO_MANY)) {
            throw EntityValidationException(
                "Relationship type '" + relationship.type + "' is invalid for mapping type '" + mapping.columnType + "' in " + entity.name + "." + field.name
            )
        }
    }

    private fun checkRelationshipAndJavaType(
        dependencyInfo: DependencyInfo,
        dependencyTpl: DependencyTpl,
        relationship: Relationship,
        relationshipOther: Relationship
    ) {
        if (Utils.not(relationship.type == Relationship.Type.ONE_TO_ONE || relationship.type == Relationship.Type.ONE_TO_MANY)) {
            throw EntityValidationException(
                "Relationship type '" + relationship.type + "' is invalid for mapping type '" + mapping.columnType + "' in relationship '" +
                        relationship.entity + "." + field.name + "' <- '" +
                        relationshipOther.entity + "'"
            )
        }
        if (field.javaType == JavaType.OBJECT) {
            if (Utils.not(
                    relationshipOther.type == Relationship.Type.ONE_TO_ONE
                            && relationship.type == Relationship.Type.ONE_TO_ONE
                )
            ) {
                throw EntityValidationException(
                    "invalid relationship type '" + relationship.type + "' in relation '" + entity.name + "." + field.name + "' -> '" + dependencyTpl.entity!!.name + "." + dependencyInfo.field.name + "'"
                )
            }
        }
        if (field.javaType == JavaType.ARRAY) {
            if (Utils.not(
                    relationshipOther.type == Relationship.Type.MANY_TO_ONE
                            && relationship.type == Relationship.Type.ONE_TO_MANY
                )
            ) {
                throw EntityValidationException(
                    "invalid relationship type '" + relationship.type + "' in relation '" + entity.name + "." + field.name + "' -> '" + dependencyTpl.entity!!.name + "'"
                )
            }
        }
    }

    private fun checkAllForeignColumnExistsInOppositeEntityDbMapping() {
        val oppositeEntity = field.relationship!!.entity
        mapping.foreignColumnMappingList
            .forEach(Consumer { foreignColumnMapping: ForeignColumnMapping ->
                val existInCombinedColumns = internalEntityValidator.columnExistsInCombinedColumns(
                    oppositeEntity,
                    foreignColumnMapping.srcColumn
                )
                if (Utils.not(existInCombinedColumns)) {
                    throw EntityValidationException("foreign column mapping is missing in " + entity.name + "." + field.name + " -> " + oppositeEntity)
                }
            })
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