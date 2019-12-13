package tai.orm.entity.columnmapping

import tai.orm.entity.ForeignColumnMapping
import tai.orm.entity.RelationType

data class ColumnMapping (
    val field: String,
    val column: String
)

interface RelationMapping {
    val field: String
    val columnType: RelationType
    val referencingTable: String
    val referencingEntity: String
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
