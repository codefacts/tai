package tai.orm.entity.impl

import tai.orm.Utils
import tai.orm.core.FieldExpression
import tai.orm.core.PathExpression
import tai.orm.entity.EntityMappingHelper
import tai.orm.entity.EntityUtils
import tai.orm.entity.core.DbMapping
import tai.orm.entity.core.Entity
import tai.orm.entity.core.Field
import tai.orm.entity.core.columnmapping.ColumnMapping
import tai.orm.entity.core.columnmapping.RelationMapping
import tai.orm.entity.ex.EntityMappingHelperExcpetion
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Stream

/**
 * Created by Jango on 2017-01-21.
 */
class EntityMappingHelperImpl(entities: Collection<Entity>) :
    EntityMappingHelper {
    val entityMap: Map<String, Entity>
    val tableToEntityMap: Map<String, Entity>
    override fun getEntity(entity: String): Entity {
        return entityMap[entity] ?: throw NullPointerException("No Entity found for key '$entity'")
    }

    override fun getEntityByTable(table: String): Entity {
        return tableToEntityMap[table] ?: throw NullPointerException("No Entity found for dependentTable '$table'")
    }

    override fun getFields(entity: String): Array<Field> {
        return getEntity(entity).fields
    }

    override fun getDbMapping(entity: String): DbMapping {
        return getEntity(entity).dbMapping
    }

    override fun getColumnMappings(entity: String): Array<ColumnMapping> {
        return getEntity(entity).dbMapping.columnMappings
    }

    override fun getFieldNameToFieldMap(entity: String): Map<String, Field> {
        return getEntity(entity).fields.asSequence().map { it.name to it }.toMap()
    }

    override fun getColumnNameToColumnMappingMap(entity: String): Map<String, ColumnMapping> {
        return getEntity(entity).dbMapping.columnMappings.asSequence().map { it.column to it }.toMap()
    }

    override fun getFieldToColumnMappingMap(entity: String): Map<String, ColumnMapping> {
        return getEntity(entity).dbMapping.columnMappings.asSequence().map { it.field to it }.toMap()
    }

    override fun getField(entity: String, field: String): Field {
        return Arrays.stream(getEntity(entity).fields)
            .filter { ff: Field ->
                ff.name == field
            }
            .findAny()
            .orElseThrow { EntityMappingHelperExcpetion("Field '$field' does not exists in '$entity'") }
    }

    override fun getFieldByColumn(entity: String, column: String): Field {
        return Stream.of(*getDbMapping(entity).columnMappings)
            .map { dbColumnMapping: ColumnMapping -> dbColumnMapping }
            .filter(Predicate<ColumnMapping> { simpleDbColumnMapping: ColumnMapping ->
                simpleDbColumnMapping.column.equals(
                    column
                )
            })
            .map(Function<ColumnMapping, Field> { simpleDbColumnMapping: ColumnMapping ->
                getField(
                    entity,
                    simpleDbColumnMapping.field
                )
            })
            .findAny()
            .orElseThrow { EntityMappingHelperExcpetion("No column found for column '$column' in entity '$entity'") }
    }

    override fun getColumnMapping(entity: String, field: String): ColumnMapping {
        return Arrays.stream(getEntity(entity).dbMapping.columnMappings)
            .filter { dbColumnMapping: ColumnMapping ->
                dbColumnMapping.field.equals(
                    field
                )
            }
            .findAny()
            .orElseThrow { EntityMappingHelperExcpetion("No ColumnMapping found for column '$entity.$field'") }
    }

    override fun getRelationMapping(entity: String, field: String): RelationMapping {
        return Arrays.stream(getEntity(entity).dbMapping.relationMappings)
            .filter { dbColumnMapping: RelationMapping ->
                dbColumnMapping.field.equals(
                    field
                )
            }
            .findAny()
            .orElseThrow { EntityMappingHelperExcpetion("No ColumnMapping found for column '$entity.$field'") }
    }

    override fun getReferencingEntity(
        entity: String,
        fieldExpression: FieldExpression
    ): String {
        var entity1 = entity
        val parts = fieldExpression.toPathExpression()!!.parts()
        for (i in 1 until parts.size) {
            val part = parts[i]
            entity1 = getRelationMapping(entity1, part).referencingEntity
        }
        return entity1
    }

    override fun getPrimaryKey(entity: String): String {
        return getEntity(entity).primaryKey
    }

    override fun getPrimaryKeyColumnMapping(entity: String): ColumnMapping {
        return getColumnMapping(entity, getEntity(entity).primaryKey)
    }

    override fun getPrimaryKeyColumnName(entity: String): String {
        return getPrimaryKeyColumnMapping(entity).column
    }

    override fun exists(entity: String): Boolean {
        return entityMap.containsKey(entity)
    }

    override fun getTable(entity: String): String {
        return getEntity(entity).dbMapping.table
    }

    override fun getDbMappingByTable(table: String): DbMapping {
        return getEntityByTable(table).dbMapping
    }

    override val tables: List<String>
        get() = tableToEntityMap.keys.toList()

    override val entities: List<Entity>
        get() = entityMap.values.toList()

    override fun isMandatory(entity: String, pathExpression: PathExpression): Boolean {
        var entity1 = entity
        val parts = pathExpression.parts()
        for (i in 1 until parts.size) {
            val field = parts[i]
            val mapping = getRelationMapping(entity1, field)
            if (Utils.not(mapping!!.options!!.isMandatory)) {
                return false
            }
            entity1 = mapping.referencingEntity
        }
        return true
    }

    override fun getRelationMappings(entity: String): List<RelationMapping> {
        return Arrays.asList(*getDbMapping(entity).relationMappings)
    }

    init {
        var entities1 = entities
        Objects.requireNonNull(entities1)
        entities1 = EntityUtils.validateAndPreProcess(entities1)
        entityMap = EntityUtils.toEntityNameToEntityMap(entities1)
        tableToEntityMap = EntityUtils.toTableToEntityMap(entities1)
    }
}