package tai.orm.validation.impl

import tai.orm.validation.EntitiesValidator
import tai.orm.validation.EntityValidator
import tai.orm.entity.Entity
import tai.orm.validation.ex.EntityValidationException
import java.util.function.Consumer

/**
 * Created by sohan on 3/17/2017.
 */
data class EntitiesValidatorImpl(val entityValidator: EntityValidator) :
    EntitiesValidator {

    override fun validate(params: EntitiesValidator.Params) {
        checkTableShortCodeNoDuplicate(params.entities)
        params.entities.forEach(
            Consumer { entity: Entity ->
                entityValidator.validate(
                    EntityValidator.Params(
                        entity = entity,
                        entityNameToEntityMap = params.entityNameToEntityMap
                    )
                )
            }
        )
    }

    private fun checkTableShortCodeNoDuplicate(entities: Collection<Entity>) {
        val shortCodeToEntityMap = mutableMapOf<String, Entity>()
        entities.forEach {
            val shortCode = it.dbMapping.tableShortCode.toLowerCase()
            if (shortCode.isBlank()) {
                throw EntityValidationException("ShortCode is blank in entity '${it.name}'")
            }
            if (shortCodeToEntityMap.containsKey(shortCode)) {
                val otherEntity = shortCodeToEntityMap[shortCode]!!
                throw EntityValidationException("Duplicate table short code '${it.dbMapping.tableShortCode}' in both entity ${it.name} and ${otherEntity.name}")
            }
            shortCodeToEntityMap[shortCode] = it
        }
    }
}