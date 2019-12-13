package tai.orm.entity

import tai.orm.entity.columnmapping.ColumnMapping
import tai.orm.entity.columnmapping.RelationMapping
import tai.orm.validation.EntitiesValidator
import tai.orm.validation.impl.*

/**
 * Created by Jango on 2017-01-21.
 */
interface EntityUtils {

    companion object {

        fun toEntityNameToEntityMap(entities: Collection<Entity>): Map<String, Entity> {
            return entities.asSequence().map { it.name to it }.toMap()
        }

        fun toTableToEntityMap(entities: Collection<Entity>): Map<String, Entity> {
            return entities.asSequence().map { it.dbMapping.table to it }.toMap()
        }

        fun toFieldNameToFieldMap(fields: List<Field>): Map<String, Field> {
            return fields.asSequence().map { field -> field.name to field }.toMap()
        }

        fun validateAndPreProcess(entities: Collection<Entity>): Collection<Entity> {
            val entityMap = toEntityNameToEntityMap(entities)
            EntitiesValidatorImpl(EntityValidatorImpl()).validate(
                EntitiesValidator.Params(
                    entities = entities,
                    entityNameToEntityMap = entityMap
                )
            )
            return entities;
        }
    }
}