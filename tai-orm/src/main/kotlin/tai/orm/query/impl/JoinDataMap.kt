package tai.orm.query.impl

import tai.criteria.operators.JoinType
import tai.orm.JoinParam
import tai.orm.core.PathExpression
import tai.orm.entity.Entity
import tai.orm.entity.EntityMappingHelper
import tai.orm.query.CreateAlias
import tai.orm.query.CreateAliasIsLast
import tai.orm.query.CreateJoinData
import tai.orm.query.ex.QueryParserException

data class AliasAndEntity(val alias: String, val entity: Entity)

data class JoinData(
    val parentEntityAlias: String,
    val childEntityAlias: String,
    val parentEntity: Entity,
    val childEntityField: String,
    val childEntity: Entity,
    val joinType: JoinType? = null,
    val joinDataMap: MutableMap<String, JoinData> = mutableMapOf()
) {
    override fun toString(): String {
        return "JoinData(parentEntityAlias='$parentEntityAlias', childEntityAlias='$childEntityAlias', parentEntity=${parentEntity.name}, " +
                "childEntityField='$childEntityField', childEntity=${childEntity.name}, JoinType=$joinType)"
    }
}

class JoinDataHelper(
    val helper: EntityMappingHelper
) {

    fun populateJoinDataMap(
        rootAlias: String, rootEntity: Entity,
        fullPathExp: PathExpression, joinParam: JoinParam?,
        rootJoinDataMap: MutableMap<String, JoinData>,
        createAlias: CreateAliasIsLast
    ): AliasAndEntity {

        return traverse(rootAlias, rootEntity, fullPathExp, joinParam, rootJoinDataMap, createJoinData = CreateJoinData { parentEntityAlias, parentEntity, childEntityField, isLast ->
            val childEntity = helper.getEntity(
                helper.getChildEntity(parentEntity, childEntityField)
            )
            return@CreateJoinData JoinData(
                parentEntityAlias = parentEntityAlias,
                parentEntity = parentEntity,
                childEntityField = childEntityField,
                childEntity = childEntity,
                childEntityAlias = createAlias.create(childEntity.dbMapping.tableShortCode, isLast)
            )
        })
    }

    fun traverse(
        rootAlias: String,
        rootEntity: Entity,
        fullPathExp: PathExpression,
        joinParam: JoinParam?,
        rootJoinDataMap: MutableMap<String, JoinData>,
        createJoinData: CreateJoinData
    ): AliasAndEntity {
        var alias = rootAlias
        var entity = rootEntity
        var joinDataMap = rootJoinDataMap
        var prevJoinDataMap = rootJoinDataMap

        var joinData: JoinData? = null
        fullPathExp.parts().let { it.subList(1, it.size) }
            .forEachIndexed { index, childEntityField ->

                val isLast = index == fullPathExp.size() - 2
                joinData = joinDataMap[childEntityField] ?: createJoinData.create(alias, entity, childEntityField, isLast)

                val joinDataNonNull = joinData ?: throw QueryParserException("Assertion error: Join data should not be null")

                joinDataMap[childEntityField] = joinDataNonNull

                alias = joinDataNonNull.childEntityAlias
                entity = joinDataNonNull.childEntity
                prevJoinDataMap = joinDataMap
                joinDataMap = joinDataNonNull.joinDataMap
            }

        if (joinData != null) {
            val joinDataNN = joinData!!
            prevJoinDataMap[joinDataNN.childEntityField] = joinDataNN.copy(
                joinType = joinParam?.joinType
            )
        }

        return AliasAndEntity(alias, entity)
    }
}

fun makeCreateAlias(): CreateAlias {
    val aliasToCountMap = mutableMapOf<String, Int>()

    return CreateAlias { shortCode ->
        val count = (aliasToCountMap[shortCode] ?: 0) + 1
        aliasToCountMap[shortCode] = count
        "$shortCode$count"
    }
}