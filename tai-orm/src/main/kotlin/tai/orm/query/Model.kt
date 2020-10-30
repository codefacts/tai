package tai.orm.query

import tai.orm.entity.Entity
import tai.orm.query.impl.JoinData

data class AliasAndColumn(val alias: String, val column: String)

typealias CreateAlias = (shortCode: String) -> String

typealias CreateAliasIsLast = (shortCode: String, isLast: Boolean) -> String

typealias CreateJoinData = (
    parentEntityAlias: String, parentEntity: Entity,
    childEntityField: String, isLast: Boolean
) -> JoinData