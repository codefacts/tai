package tai.orm.entity.impl

import tai.orm.entity.DependencyInfo
import tai.orm.entity.DependencyTpl
import tai.orm.entity.core.Entity
import java.util.*

/**
 * Created by sohan on 3/17/2017.
 */
class TableDependency(tableToDependencyInfoMap: MutableMap<String, DependencyTpl>) {
    val tableToDependencyInfoMap: MutableMap<String, DependencyTpl>

    fun add(entity: Entity, dependencyInfo: DependencyInfo?) {
        val table: String = entity.dbMapping.table
        var dependencyTpl = tableToDependencyInfoMap[table]
        if (dependencyTpl == null) {
            tableToDependencyInfoMap[table] = DependencyTpl(entity, HashMap()).also {
                dependencyTpl = it
            }
        }
        dependencyTpl!!.add(dependencyInfo!!)
    }

    init {
        Objects.requireNonNull(tableToDependencyInfoMap)
        this.tableToDependencyInfoMap = tableToDependencyInfoMap
    }
}