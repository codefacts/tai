package tai.orm.update.builder

import tai.orm.entity.Entity
import tai.orm.update.OperationMap

interface OperationMapBuilder {
    fun build(entities: Collection<Entity>): OperationMap
}