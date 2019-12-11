package tai.orm.entity.core.columnmapping

import tai.orm.entity.core.RelationType

/**
 * Created by sohan on 3/17/2017.
 */
interface RelationMapping {
    val field: String
    val columnType: RelationType
    val referencingTable: String
    val referencingEntity: String
    val options: RelationMappingOptions
}