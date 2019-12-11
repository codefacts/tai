package tai.orm.entity.impl

import tai.orm.entity.EntitiesValidator
import tai.orm.entity.EntityValidator
import tai.orm.entity.core.Entity
import java.util.*
import java.util.function.Consumer

/**
 * Created by sohan on 3/17/2017.
 */
class EntitiesValidatorImpl(entityValidator: EntityValidator) : EntitiesValidator {
    val entityValidator: EntityValidator
    override fun validate(params: EntitiesValidator.Params?) {
        params!!.entities.forEach(
            Consumer { entity: Entity? ->
                entityValidator.validate(
                    EntityValidator.ParamsBuilder()
                        .setEntity(entity)
                        .setDependencyMap(
                            params.tableToTableDependencyMap
                        )
                        .setEntityNameToEntityMap(
                            params.entityNameToEntityMap
                        )
                        .createParams()
                )
            }
        )
    }

    init {
        Objects.requireNonNull(entityValidator)
        this.entityValidator = entityValidator
    }
}