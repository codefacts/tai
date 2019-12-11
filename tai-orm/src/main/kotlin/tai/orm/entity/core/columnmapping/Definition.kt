package tai.orm.entity.core.columnmapping

import tai.orm.entity.core.ForeignColumnMapping
import tai.orm.entity.core.RelationType

data class ColumnMapping (
    val field: String,
    val column: String
)

interface RelationMapping {
    val field: String
    val columnType: RelationType
    val referencingTable: String
    val referencingEntity: String
    val options: RelationMappingOptions
}

interface DirectRelationMapping : RelationMapping {
    val foreignColumnMappingList: List<ForeignColumnMapping>
}

interface IndirectRelationMapping : RelationMapping {
    val relationTable: String
    val srcForeignColumnMappingList: List<ForeignColumnMapping>
    val dstForeignColumnMappingList: List<ForeignColumnMapping>
}

interface VirtualRelationMapping : RelationMapping {
    val foreignColumnMappingList: List<ForeignColumnMapping>
}

interface RelationMappingOptions {
    val cascadeUpsert: CascadeUpsert
    val cascadeDelete: CascadeDelete
    val isMandatory: Boolean

    enum class CascadeUpsert {
        YES, NO
    }

    enum class CascadeDelete {
        YES, NO
    }
}