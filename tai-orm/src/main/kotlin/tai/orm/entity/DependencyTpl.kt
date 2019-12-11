package tai.orm.entity

import tai.orm.entity.core.Entity
import java.util.*

/**
 * Created by sohan on 3/17/2017.
 */
class DependencyTpl(
    entity: Entity,
    fieldToDependencyInfoMap: MutableMap<String, DependencyInfo>
) {
    val entity: Entity
    val fieldToDependencyInfoMap: MutableMap<String, DependencyInfo>

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as DependencyTpl
        if (if (entity != null) entity != that.entity else that.entity != null) return false
        return if (fieldToDependencyInfoMap != null) fieldToDependencyInfoMap == that.fieldToDependencyInfoMap else that.fieldToDependencyInfoMap == null
    }

    override fun hashCode(): Int {
        var result = entity?.hashCode() ?: 0
        result = 31 * result + (fieldToDependencyInfoMap?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "DependencyTpl{" +
                "entity=" + entity +
                ", fieldToDependencyInfoMap=" + fieldToDependencyInfoMap +
                '}'
    }

    fun add(dependencyInfo: DependencyInfo) {
        fieldToDependencyInfoMap!![dependencyInfo.field.name] = dependencyInfo
    }

    init {
        Objects.requireNonNull(entity)
        Objects.requireNonNull(fieldToDependencyInfoMap)
        this.entity = entity
        this.fieldToDependencyInfoMap = fieldToDependencyInfoMap
    }
}