package tai.orm.validation

import tai.orm.entity.Entity

/**
 * Created by sohan on 3/17/2017.
 */
@FunctionalInterface
interface EntitiesValidator {

    fun validate(params: Params)

    data class Params(
        val entities: Collection<Entity>,
        val entityNameToEntityMap: Map<String, Entity>
    )
}