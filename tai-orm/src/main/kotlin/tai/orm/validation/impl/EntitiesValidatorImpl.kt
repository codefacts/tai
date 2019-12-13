package tai.orm.validation.impl

import tai.orm.validation.EntitiesValidator
import tai.orm.validation.EntityValidator
import tai.orm.entity.Entity
import java.util.function.Consumer

/**
 * Created by sohan on 3/17/2017.
 */
data class EntitiesValidatorImpl(val entityValidator: EntityValidator) :
    EntitiesValidator {

    override fun validate(params: EntitiesValidator.Params) {
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
}