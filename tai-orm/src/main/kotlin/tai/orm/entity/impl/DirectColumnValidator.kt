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
    val internalEntityValidator: InternalEntityValidator,
    val entity: Entity,
    val field: Field,
    val mapping: DirectRelationMapping
) {

    fun validate() {
        checkFieldType()
        val relationship = field.relationship ?: throw EntityValidationException("No relationship present for mapping '" + mapping + "' in table '" + entity.dbMapping.table + "'")
        val dependencyTplOptional = internalEntityValidator.checkRelationalValidity(mapping)
        dependencyTplOptional.flatMap { dependencyTpl: DependencyTpl ->
            dependencyTpl.fieldToDependencyInfoMap.values.stream()
                .filter { dependencyInfo: DependencyInfo ->
                    findDependencyInfoInOppositeSide(
                        dependencyInfo
                    )
                }
                .findAny()
        }.ifPresent { dependencyInfo: DependencyInfo ->
            val dependencyTpl = dependencyTplOptional.get()
            val relationshipOther = dependencyInfo.field.relationship ?: throw EntityValidationException("No relationship present for mapping '" + mapping + "' in table '" + dependencyTpl.entity!!.dbMapping.table + "'")
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

    private fun checkFieldType() { //        boolean isFieldTypeOk = field.getJavaType() == JavaType.OBJECT;
//
//        if (Utils.not(isFieldTypeOk)) {
//            throw new EntityValidationException("Field '" + field.getName() + "' has an invalid type '" + field.getJavaType() + "' for dbColumnMappingType '"
//                + mapping.getColumnType() + "' in entity '" + entity.getName() + "'");
//        }
    }
}