package tai.orm.entity.impl

import tai.orm.entity.EntitiesValidator
import tai.orm.entity.EntityValidator
import tai.orm.entity.core.Entity
import java.util.*
import java.util.function.Consumer

/**
 * Created by sohan on 3/17/2017.
 */
data class EntitiesValidatorImpl(val entityValidator: EntityValidator) : EntitiesValidator {

    override fun validate(params: EntitiesValidator.Params) {
        params.entities.forEach(
            Consumer { entity: Entity ->
                entityValidator.validate(
                    EntityValidator.Params(
                        entity = entity,
                        tableToTabledependencyMap = params.tableToTableDependencyMap,
                        entityNameToEntityMap = params.entityNameToEntityMap
                    )
                )
            }
        )
    }
}