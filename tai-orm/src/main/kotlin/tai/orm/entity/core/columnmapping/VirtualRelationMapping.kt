package tai.orm.entity.core.columnmapping

import tai.orm.entity.core.ForeignColumnMapping

/**
 * Created by Jango on 2017-01-12.
 */
interface VirtualRelationMapping : RelationMapping {
    val foreignColumnMappingList: List<ForeignColumnMapping>
}