package tai.orm.entity

import tai.orm.core.FieldExpression
import tai.orm.core.PathExpression
import tai.orm.entity.core.DbMapping
import tai.orm.entity.core.Entity
import tai.orm.entity.core.Field
import tai.orm.entity.core.columnmapping.ColumnMapping
import tai.orm.entity.core.columnmapping.RelationMapping

/**
 * Created by Jango on 2017-01-08.
 */
interface EntityMappingHelper {

    fun getEntity(entity: String): Entity
    fun getEntityByTable(table: String): Entity
    fun getFields(entity: String): List<Field>
    fun getDbMapping(entity: String): DbMapping
    fun getColumnMappings(entity: String): List<ColumnMapping>
    fun getFieldNameToFieldMap(entity: String): Map<String, Field>
    fun getColumnNameToColumnMappingMap(entity: String): Map<String, ColumnMapping>
    fun getFieldToColumnMappingMap(entity: String): Map<String, ColumnMapping>
    fun getField(entity: String, field: String): Field
    fun getFieldByColumn(entity: String, column: String): Field
    fun getColumnMapping(entity: String, field: String): ColumnMapping
    fun getRelationMapping(entity: String, field: String): RelationMapping
    fun getReferencingEntity(
        entity: String,
        fieldExpression: FieldExpression
    ): String

    fun getPrimaryKey(entity: String): String
    fun getPrimaryKeyColumnMapping(entity: String): ColumnMapping
    fun getPrimaryKeyColumnName(entity: String): String
    fun exists(entity: String): Boolean
    fun getTable(entity: String): String
    fun getDbMappingByTable(table: String): DbMapping
    val tables: List<String>
    val entities: List<Entity>
    fun isMandatory(entity: String, parent: PathExpression): Boolean
    fun getRelationMappings(entity: String): List<RelationMapping>
}