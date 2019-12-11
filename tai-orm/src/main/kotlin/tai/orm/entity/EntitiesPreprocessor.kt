package tai.orm.entity

import tai.orm.entity.core.Entity
import tai.orm.entity.impl.TableDependency
import java.util.*

/**
 * Created by sohan on 3/17/2017.
 */
interface EntitiesPreprocessor {
    fun process(params: Params): List<Entity>
    class Params(
        entities: Collection<Entity>,
        tableToTableDependencyMap: Map<String, TableDependency>,
        entityNameToEntityMap: Map<String, Entity>
    ) {
        val entities: Collection<Entity>
        val tableToTableDependencyMap: Map<String, TableDependency>
        val entityNameToEntityMap: Map<String, Entity>

        init {
            Objects.requireNonNull(entities)
            Objects.requireNonNull(tableToTableDependencyMap)
            Objects.requireNonNull(entityNameToEntityMap)
            this.entities = entities
            this.tableToTableDependencyMap = tableToTableDependencyMap
            this.entityNameToEntityMap = entityNameToEntityMap
        }
    }
}