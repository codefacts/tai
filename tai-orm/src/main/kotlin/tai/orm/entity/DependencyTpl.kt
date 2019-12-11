package tai.orm.entity

import tai.orm.entity.core.Entity
import java.util.*

/**
 * Created by sohan on 3/17/2017.
 */
data class DependencyTpl(
    val entity: Entity,
    val fieldToDependencyInfoMap: MutableMap<String, DependencyInfo>
) {
    fun add(dependencyInfo: DependencyInfo) {
        fieldToDependencyInfoMap[dependencyInfo.field.name] = dependencyInfo
    }
}