package tai.orm.entity.columnmapping

import tai.criteria.operators.JoinType
import tai.orm.entity.ForeignColumnMapping
import tai.orm.entity.RelationType
import tai.orm.validation.ex.EntityValidationException

/**
 * Created by Jango on 2017-01-12.
 */

data class DirectRelationMappingImpl(
    override val field: String,
    override val referencingTable: String,
    override val referencingEntity: String,
    override val foreignColumnMappingList: List<ForeignColumnMapping>,
    override val joinType: JoinType = JoinType.JOIN
) : DirectRelationMapping {
    override val columnType: RelationType = RelationType.DIRECT
}

data class IndirectRelationMappingImpl(
    override val field: String,
    override val referencingTable: String,
    override val referencingEntity: String,
    override val relationTable: String,
    override val srcForeignColumnMappingList: List<ForeignColumnMapping>,
    override val dstForeignColumnMappingList: List<ForeignColumnMapping>,
    override val srcJoinType: JoinType = JoinType.JOIN,
    override val dstJoinType: JoinType = JoinType.JOIN
) : IndirectRelationMapping {
    override val columnType: RelationType = RelationType.INDIRECT
}

data class VirtualRelationMappingImpl(
    override val field: String,
    override val referencingTable: String,
    override val referencingEntity: String,
    override val foreignColumnMappingList: List<ForeignColumnMapping>,
    override val joinType: JoinType = JoinType.JOIN
) : VirtualRelationMapping {
    override val columnType: RelationType = RelationType.VIRTUAL
}