package tai.orm.entity.core

import tai.orm.entity.core.columnmapping.ColumnMapping
import tai.orm.entity.core.columnmapping.RelationMapping

data class Column(val name: String)

data class DbMapping(
    val table: String,
    val primaryColumn: String,
    val columnMappings: List<ColumnMapping>,
    val relationMappings: List<RelationMapping>
)

data class Entity(
    val name: String,
    val primaryKey: String,
    val fields: List<Field>,
    val dbMapping: DbMapping
)

data class Field(
    val name: String,
    val relationship: Relationship? = null
)

data class ForeignColumnMapping(val srcColumn: String, val dstColumn: String)

data class Relationship(
    val name: Name,
    val entity: String
) {
    enum class Name {
        HAS_ONE, HAS_MANY
    }
}

enum class RelationType {
    VIRTUAL, DIRECT, INDIRECT
}