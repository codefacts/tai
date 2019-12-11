package tai.orm.entity.core.columnmapping.impl

import tai.orm.entity.core.ForeignColumnMapping
import tai.orm.entity.core.RelationType
import tai.orm.entity.core.columnmapping.RelationMappingOptions
import tai.orm.entity.core.columnmapping.VirtualRelationMapping
import java.util.*

/**
 * Created by Jango on 2017-01-12.
 */
class VirtualRelationMappingImpl(
    override val referencingTable: String,
    override val referencingEntity: String,
    override val foreignColumnMappingList: List<ForeignColumnMapping>,
    override val field: String,
    override val options: RelationMappingOptions
) : VirtualRelationMapping {
    override val columnType: RelationType = RelationType.VIRTUAL

}