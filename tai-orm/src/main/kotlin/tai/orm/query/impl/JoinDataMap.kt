package tai.orm.query.impl

import tai.orm.core.PathExpression
import tai.orm.entity.Entity
import tai.orm.entity.EntityMappingHelper

data class AliasAndEntity(val alias: String, val entity: Entity)

data class JoinData(
    val parentEntityAlias: String,
    val childEntityAlias: String,
    val parentEntity: Entity,
    val childEntityField: String,
    val childEntity: Entity,
    val joinDataMap: MutableMap<String, JoinData> = mutableMapOf()
) {
    override fun toString(): String {
        return "JoinData(parentEntityAlias='$parentEntityAlias', childEntityAlias='$childEntityAlias', parentEntity=${parentEntity.name}, " +
                "childEntityField='$childEntityField', childEntity=${childEntity.name}, joinDataMap=$joinDataMap)"
    }
}

class JoinDataHelper(
    val helper: EntityMappingHelper
) {

    fun populateJoinDataMap(
        rootAlias: String, rootEntity: Entity,
        fullPathExp: PathExpression, rootJoinDataMap: MutableMap<String, JoinData>,
        createAlias: CreateAliasIsLast
    ): AliasAndEntity {

        return traverse(rootAlias, rootEntity, fullPathExp, rootJoinDataMap) { parentEntityAlias, parentEntity, childEntityField, isLast ->
            val childEntity = helper.getEntity(
                helper.getChildEntity(parentEntity, childEntityField)
            )
            return@traverse JoinData(
                parentEntityAlias = parentEntityAlias,
                parentEntity = parentEntity,
                childEntityField = childEntityField,
                childEntity = childEntity,
                childEntityAlias = createAlias(childEntity.dbMapping.tableShortCode, isLast)
            )
        }
    }

    fun traverse(
        rootAlias: String,
        rootEntity: Entity,
        fullPathExp: PathExpression,
        rootJoinDataMap: MutableMap<String, JoinData>,
        createJoinData: CreateJoinData
    ): AliasAndEntity {
        var alias = rootAlias
        var entity = rootEntity
        var joinDataMap = rootJoinDataMap

        fullPathExp.parts().let { it.subList(1, it.size) }
            .forEachIndexed { index, childEntityField ->

                val isLast = index == fullPathExp.size() - 2
                val joinData = joinDataMap[childEntityField] ?: createJoinData(alias, entity, childEntityField, isLast)

                joinDataMap[childEntityField] = joinData

                alias = joinData.childEntityAlias
                entity = joinData.childEntity
                joinDataMap = joinData.joinDataMap
            }
        return AliasAndEntity(alias, entity)
    }
}

fun makeCreateAlias(): CreateAlias {
    val aliasToCountMap = mutableMapOf<String, Int>()

    return { shortCode ->
        val count = (aliasToCountMap[shortCode] ?: 0) + 1
        aliasToCountMap[shortCode] = count
        "$shortCode$count"
    }
}

typealias CreateAlias = (shortCode: String) -> String
typealias CreateAliasIsLast = (shortCode: String, isLast: Boolean) -> String

typealias CreateJoinData = (
    parentEntityAlias: String, parentEntity: Entity, childEntityField: String, isLast: Boolean
) -> JoinData