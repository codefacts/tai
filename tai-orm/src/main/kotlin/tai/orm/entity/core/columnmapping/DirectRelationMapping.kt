package tai.orm.entity.core.columnmapping

import tai.orm.entity.core.ForeignColumnMapping

/**
 * Created by Jango on 2017-01-12.
 */
interface DirectRelationMapping : RelationMapping {
    abstract override val referencingTable: String
    abstract override val referencingEntity: String
    val foreignColumnMappingList: List<ForeignColumnMapping>
    abstract override val options: DirectRelationMappingOptions
}