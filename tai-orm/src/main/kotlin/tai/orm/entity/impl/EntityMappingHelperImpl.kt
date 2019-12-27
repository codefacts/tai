package tai.orm.entity.impl

import tai.orm.ex.OrmException
import tai.orm.Utils
import tai.orm.core.FieldExpression
import tai.orm.core.PathExpression
import tai.orm.entity.EntityMappingHelper
import tai.orm.entity.EntityUtils
import tai.orm.entity.DbMapping
import tai.orm.entity.Entity
import tai.orm.entity.Field
import tai.orm.entity.columnmapping.ColumnMapping
import tai.orm.entity.columnmapping.RelationMapping
import tai.orm.validation.ex.EntityMappingHelperException
import java.util.function.Function
import java.util.function.Predicate

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

    override fun getFields(entity: String): List<Field> {
        return getEntity(entity).fields
    }

    override fun getDbMapping(entity: String): DbMapping {
        return getEntity(entity).dbMapping
    }

    override fun getColumnMappings(entity: String): List<ColumnMapping> {
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
        return getField(getEntity(entity), field)
    }

    override fun getField(entity: Entity, field: String): Field {
        return entity.fields.stream()
            .filter { ff: Field ->
                ff.name == field
            }
            .findAny()
            .orElseThrow { EntityMappingHelperException("Field '$field' does not exists in '${entity.name}'") }
    }

    override fun getFieldByColumn(entity: String, column: String): Field {
        return getDbMapping(entity).columnMappings.stream()
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
            .orElseThrow { EntityMappingHelperException("No column found for column '$column' in entity '$entity'") }
    }

    override fun getColumnMapping(entity: String, field: String): ColumnMapping {
        return getColumnMapping(getEntity(entity), field)
    }

    override fun getColumnMapping(entity: Entity, field: String): ColumnMapping {
        return entity.dbMapping.columnMappings.stream()
            .filter { dbColumnMapping: ColumnMapping ->
                dbColumnMapping.field.equals(
                    field
                )
            }
            .findAny()
            .orElseThrow { EntityMappingHelperException("No ColumnMapping exists for column '${entity.name}.$field'") }
    }

    override fun getRelationMapping(entity: String, field: String): RelationMapping {
        return getRelationMapping(entity, field)
    }

    override fun getRelationMapping(entity: Entity, field: String): RelationMapping {
        return entity.dbMapping.relationMappings.stream()
            .filter { dbColumnMapping: RelationMapping ->
                dbColumnMapping.field.equals(
                    field
                )
            }
            .findAny()
            .orElseThrow { EntityMappingHelperException("No ColumnMapping found for column '${entity.name}.$field'") }
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
            val field = getField(entity1, parts[i])
            if (Utils.not(field.relationship?.options?.isNonNull == true)) {
                return false
            }
            entity1 = field.relationship?.entity ?: throw OrmException("Relationship does not exists on field $field in $entity.$pathExpression")
        }
        return true
    }

    override fun getRelationMappings(entity: String): List<RelationMapping> {
        return getDbMapping(entity).relationMappings
    }

    override fun getChildEntity(parentEntity: Entity, childEntityField: String): String {
        return getField(parentEntity, childEntityField).relationship?.entity ?: throw OrmException(
            "No relationship found at ${parentEntity.name}.$childEntityField"
        )
    }

    init {
        entityMap = EntityUtils.toEntityNameToEntityMap(entities)
        tableToEntityMap = EntityUtils.toTableToEntityMap(entities)
    }
}