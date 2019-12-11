package tai.orm.entity.impl

import tai.orm.Utils
import tai.orm.entity.DependencyInfo
import tai.orm.entity.DependencyTpl
import tai.orm.entity.core.*
import tai.orm.entity.core.columnmapping.DirectRelationMapping
import tai.orm.entity.core.columnmapping.VirtualRelationMapping
import tai.orm.entity.ex.EntityValidationException
import java.util.*

/**
 * Created by sohan on 4/14/2017.
 */
internal class DirectColumnValidator(
    internalEntityValidator: InternalEntityValidator,
    entity: Entity,
    field: Field,
    mapping: DirectRelationMapping
) {
    val internalEntityValidator: InternalEntityValidator
    val entity: Entity
    val field: Field
    val mapping: DirectRelationMapping
    fun validate() {
        checkFieldType()
        val relationship = field.relationship ?: throw EntityValidationException("No relationship present for mapping '" + mapping + "' in table '" + entity.dbMapping.table + "'")
        internalEntityValidator.checkFieldTypeAndName(relationship, field)
        checkRelationshipType(relationship)
        val dependencyTplOptional = internalEntityValidator.checkRelationalValidity(mapping)
        dependencyTplOptional.flatMap { dependencyTpl: DependencyTpl ->
            dependencyTpl.getFieldToDependencyInfoMap()!!.values.stream()
                .filter { dependencyInfo: DependencyInfo ->
                    findDependencyInfoInOppositeSide(
                        dependencyInfo
                    )
                }
                .findAny()
        }.ifPresent { dependencyInfo: DependencyInfo ->
            val dependencyTpl = dependencyTplOptional.get()
            val relationshipOther = dependencyInfo.field.relationship ?: throw EntityValidationException("No relationship present for mapping '" + mapping + "' in table '" + dependencyTpl.entity!!.dbMapping.table + "'")
            checkRelationshipTypeAndJavaType(dependencyInfo, dependencyTpl, relationship, relationshipOther)
        }
    }

    private fun checkRelationshipTypeAndJavaType(
        dependencyInfo: DependencyInfo,
        dependencyTpl: DependencyTpl,
        relationship: Relationship,
        relationshipOther: Relationship
    ) {
        if (dependencyInfo.field.javaType == JavaType.OBJECT) {
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
        if (dependencyInfo.field.javaType == JavaType.ARRAY) {
            if (Utils.not(
                    relationshipOther.type == Relationship.Type.ONE_TO_MANY
                            && relationship.type == Relationship.Type.MANY_TO_ONE
                )
            ) {
                throw EntityValidationException(
                    "invalid relationship type '" + relationship.type + "' in relation '" + entity.name + "." + field.name + "' -> '" + dependencyTpl.entity!!.name + "." + dependencyInfo.field.name + "'"
                )
            }
        }
    }

    private fun findDependencyInfoInOppositeSide(dependencyInfo: DependencyInfo): Boolean {
        if (dependencyInfo.relationMapping.columnType == RelationType.VIRTUAL) {
            val depMapping = dependencyInfo.relationMapping as VirtualRelationMapping
            return internalEntityValidator.isEqualDirectAndVirtual(
                mapping.foreignColumnMappingList, depMapping.foreignColumnMappingList
            )
        }
        return false
    }

    private fun checkRelationshipType(relationship: Relationship) {
        if (Utils.not(relationship.type == Relationship.Type.ONE_TO_ONE || relationship.type == Relationship.Type.MANY_TO_ONE)) {
            throw EntityValidationException(
                "Relationship type '" + relationship.type + "' is invalid for mapping type '" + mapping.columnType + "' in '" + entity.name + "." + field.name + "'"
            )
        }
    }

    private fun checkFieldType() { //        boolean isFieldTypeOk = field.getJavaType() == JavaType.OBJECT;
//
//        if (Utils.not(isFieldTypeOk)) {
//            throw new EntityValidationException("Field '" + field.getName() + "' has an invalid type '" + field.getJavaType() + "' for dbColumnMappingType '"
//                + mapping.getColumnType() + "' in entity '" + entity.getName() + "'");
//        }
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