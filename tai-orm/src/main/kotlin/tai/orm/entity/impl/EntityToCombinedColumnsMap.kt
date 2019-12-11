package tai.orm.entity.impl

import tai.orm.entity.core.DbMapping
import tai.orm.entity.core.Entity
import tai.orm.entity.core.ForeignColumnMapping
import tai.orm.entity.core.columnmapping.ColumnMapping
import tai.orm.entity.core.columnmapping.DirectRelationMapping
import tai.orm.entity.core.columnmapping.RelationMapping
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Created by sohan on 4/15/2017.
 */
internal class EntityToCombinedColumnsMap(entityNameToEntityMap: Map<String, Entity>) {
    val entityNameToEntityMap: Map<String, Entity>
    val entityToCombinedColumnsMap: MutableMap<String, Set<String>>
    fun exists(entity: String, column: String): Boolean {
        var combinedColumns: Set<String> = entityToCombinedColumnsMap[entity]!!
        if (combinedColumns == null) {
            entityToCombinedColumnsMap[entity] = combinedColumns(entity).also {
                combinedColumns = it
            }
        }
        return combinedColumns!!.contains(column)
    }

    private fun combinedColumns(entity: String): Set<String> {
        val dbMapping = entityNameToEntityMap[entity]!!.dbMapping
        return columnMappings(dbMapping) + directColumnMappings(dbMapping)
    }

    private fun directColumnMappings(dbMapping: DbMapping): Set<String> {
        return dbMapping.relationMappings.asSequence()
            .filter { relationMapping: RelationMapping -> relationMapping is DirectRelationMapping }
            .map { relationMapping: RelationMapping -> relationMapping as DirectRelationMapping }
            .flatMap { columnMapping: DirectRelationMapping ->
                columnMapping.foreignColumnMappingList.asSequence().map(ForeignColumnMapping::srcColumn)
            }
            .toSet()
    }

    private fun columnMappings(dbMapping: DbMapping): Set<String> {
        return Arrays.stream(dbMapping.columnMappings)
            .map<String>(ColumnMapping::column)
            .collect(Collectors.toSet())
    }

    init {
        Objects.requireNonNull(entityNameToEntityMap)
        this.entityNameToEntityMap = entityNameToEntityMap
        entityToCombinedColumnsMap = HashMap()
    }
}