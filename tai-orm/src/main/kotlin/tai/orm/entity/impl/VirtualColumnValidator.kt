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
import javax.management.relation.Relation

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
        val relationship = field.relationship ?: throw EntityValidationException("No relationship present for mapping '" + mapping + "' in table '" + entity.dbMapping.table + "'")
        val dependencyTplOptional = internalEntityValidator.checkRelationalValidity(mapping)
        dependencyTplOptional
            .flatMap { dependencyTpl: DependencyTpl ->
                dependencyTpl.fieldToDependencyInfoMap.values.stream()
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