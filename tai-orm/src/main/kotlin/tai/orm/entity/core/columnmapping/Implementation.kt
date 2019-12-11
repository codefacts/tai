package tai.orm.entity.core.columnmapping

import tai.orm.entity.core.ForeignColumnMapping
import tai.orm.entity.core.RelationType
import tai.orm.entity.core.columnmapping.*

/**
 * Created by Jango on 2017-01-12.
 */

data class DirectRelationMappingImpl(
    override val referencingTable: String,
    override val referencingEntity: String,
    override val foreignColumnMappingList: List<ForeignColumnMapping>,
    override val field: String,
    override val options: RelationMappingOptions
) : DirectRelationMapping {
    override val columnType: RelationType = RelationType.DIRECT
}

data class IndirectRelationMappingImpl(
    override val referencingTable: String,
    override val referencingEntity: String,
    override val relationTable: String,
    override val srcForeignColumnMappingList: List<ForeignColumnMapping>,
    override val dstForeignColumnMappingList: List<ForeignColumnMapping>,
    override val field: String,
    override val options: RelationMappingOptions
) : IndirectRelationMapping {
    override val columnType: RelationType = RelationType.INDIRECT
}

data class RelationMappingOptionsImpl(
    override val cascadeUpsert: RelationMappingOptions.CascadeUpsert = RelationMappingOptions.CascadeUpsert.NO,
    override val cascadeDelete: RelationMappingOptions.CascadeDelete = RelationMappingOptions.CascadeDelete.NO,
    override val isMandatory: Boolean = false
) : RelationMappingOptions

data class VirtualRelationMappingImpl(
    override val referencingTable: String,
    override val referencingEntity: String,
    override val foreignColumnMappingList: List<ForeignColumnMapping>,
    override val field: String,
    override val options: RelationMappingOptions
) : VirtualRelationMapping {
    override val columnType: RelationType = RelationType.VIRTUAL
}