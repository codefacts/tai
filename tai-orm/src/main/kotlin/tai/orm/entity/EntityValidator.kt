package tai.orm.entity

import tai.orm.entity.core.Entity
import tai.orm.entity.impl.TableDependency
import java.util.*

/**
 * Created by sohan on 3/17/2017.
 */
interface EntityValidator {
    fun validate(params: Params?)
    class Params(
        entity: Entity,
        tableToTabledependencyMap: Map<String, TableDependency>,
        entityNameToEntityMap: Map<String, Entity>
    ) {
        val entity: Entity
        val tableToTabledependencyMap: Map<String, TableDependency>
        val entityNameToEntityMap: Map<String, Entity>

        init {
            Objects.requireNonNull(entity)
            Objects.requireNonNull(tableToTabledependencyMap)
            Objects.requireNonNull(entityNameToEntityMap)
            this.entity = entity
            this.tableToTabledependencyMap = tableToTabledependencyMap
            this.entityNameToEntityMap = entityNameToEntityMap
        }
    }

    class ParamsBuilder {
        private var entity: Entity? = null
        private var dependencyMap: Map<String, TableDependency>? = null
        private var entityNameToEntityMap: Map<String, Entity>? = null
        fun setEntity(entity: Entity?): ParamsBuilder {
            this.entity = entity
            return this
        }

        fun setDependencyMap(dependencyMap: Map<String, TableDependency>?): ParamsBuilder {
            this.dependencyMap = dependencyMap
            return this
        }

        fun setEntityNameToEntityMap(entityNameToEntityMap: Map<String, Entity>?): ParamsBuilder {
            this.entityNameToEntityMap = entityNameToEntityMap
            return this
        }

        fun createParams(): Params {
            return Params(entity!!, dependencyMap!!, entityNameToEntityMap!!)
        }
    }
}