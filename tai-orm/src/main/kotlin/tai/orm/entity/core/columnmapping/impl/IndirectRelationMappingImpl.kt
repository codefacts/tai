package tai.orm.entity.core.columnmapping.impl

import tai.orm.entity.core.ForeignColumnMapping
import tai.orm.entity.core.RelationType
import tai.orm.entity.core.columnmapping.IndirectRelationMapping
import tai.orm.entity.core.columnmapping.RelationMappingOptions

/**
 * Created by Jango on 2017-01-12.
 */
class IndirectRelationMappingImpl(
    override val referencingTable: String,
    override val referencingEntity: String,
    override val relationTable: String,
    override val srcForeignColumnMappingList: List<ForeignColumnMapping>,
    override val dstForeignColumnMappingList: List<ForeignColumnMapping>,
    override val field: String,
    options: RelationMappingOptions
) : IndirectRelationMapping {
    override val columnType: RelationType
    override val options: RelationMappingOptions

    init {
        columnType = RelationType.INDIRECT
        this.options = options
    }
}