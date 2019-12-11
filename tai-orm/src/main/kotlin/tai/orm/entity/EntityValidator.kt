package tai.orm.entity

import tai.orm.entity.core.Entity
import tai.orm.entity.impl.TableDependency
import java.util.*

/**
 * Created by sohan on 3/17/2017.
 */
interface EntityValidator {

    fun validate(params: Params)

    data class Params(
        val entity: Entity,
        val tableToTabledependencyMap: Map<String, TableDependency>,
        val entityNameToEntityMap: Map<String, Entity>
    )
}