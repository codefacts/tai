package tai.orm.entity.core.columnmapping.impl

import tai.orm.entity.core.ForeignColumnMapping
import tai.orm.entity.core.RelationType
import tai.orm.entity.core.columnmapping.DirectRelationMapping
import tai.orm.entity.core.columnmapping.DirectRelationMappingOptions

/**
 * Created by Jango on 2017-01-12.
 */
class DirectRelationMappingImpl(
    override val referencingTable: String,
    override val referencingEntity: String,
    override val foreignColumnMappingList: List<ForeignColumnMapping>,
    override val field: String,
    options: DirectRelationMappingOptions
) : DirectRelationMapping {
    override val columnType: RelationType
    override val options: DirectRelationMappingOptions

    init {
        columnType = RelationType.DIRECT
        this.options = options
    }
}