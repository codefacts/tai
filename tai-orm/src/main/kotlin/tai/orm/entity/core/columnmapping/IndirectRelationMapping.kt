package tai.orm.entity.core.columnmapping

import tai.orm.entity.core.ForeignColumnMapping

/**
 * Created by Jango on 2017-01-12.
 */
interface IndirectRelationMapping : RelationMapping {
    abstract override val referencingTable: String
    abstract override val referencingEntity: String
    val relationTable: String?
    val srcForeignColumnMappingList: List<ForeignColumnMapping>
    val dstForeignColumnMappingList: List<ForeignColumnMapping>
    abstract override val options: RelationMappingOptions
}