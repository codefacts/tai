package tai.orm.entity

import tai.orm.entity.columnmapping.ColumnMapping
import tai.orm.entity.columnmapping.RelationMapping

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

data class DbMapping(
    val table: String,
    val primaryColumn: String,
    val tableShortCode: String,
    val columnMappings: List<ColumnMapping>,
    val relationMappings: List<RelationMapping>
)

data class ForeignColumnMapping(val srcColumn: String, val dstColumn: String)

data class Relationship(
    val name: Name,
    val entity: String,
    val options: Options = Options()
) {
    enum class Name {
        HAS_ONE, HAS_MANY
    }

    data class Options(
        val cascadeUpsert: Boolean = false,
        val cascadeDelete: Boolean = false,
        val separateLoading: Boolean = false
    )
}

enum class RelationType {
    VIRTUAL, DIRECT, INDIRECT
}