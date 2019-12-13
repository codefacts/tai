package tai.orm.validation

import tai.orm.entity.Entity

/**
 * Created by sohan on 3/17/2017.
 */
interface EntityValidator {

    fun validate(params: Params)

    data class Params(
        val entity: Entity,
        val entityNameToEntityMap: Map<String, Entity>
    )
}