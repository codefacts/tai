package tai.orm.query.impl

import tai.base.assertOrThrow
import tai.orm.entity.Entity
import tai.orm.entity.EntityMappingHelper
import tai.orm.entity.columnmapping.DirectRelationMapping
import tai.orm.entity.columnmapping.IndirectRelationMapping
import tai.orm.entity.columnmapping.VirtualRelationMapping
import tai.orm.query.CreateAlias
import tai.orm.query.ex.QueryParserException
import tai.sql.ColumnSpec
import tai.sql.FromSpec
import tai.sql.JoinRule
import tai.sql.JoinSpec

const val RELATION_TABLE_ALIAS = "rll__"

class JoinDataToJoinSpecBuilder(
    val helper: EntityMappingHelper,
    val rootEntity: Entity,
    val rootAlias: String
) {

    fun translateFrom(aliasToJoinDataMap: Map<String, MutableMap<String, JoinData>>): FromSpec {
        val joinSpecs = mutableListOf<JoinSpec>()
        val createAlias = makeCreateRelationAlias()
        aliasToJoinDataMap.forEach { (_, joinDataMap) ->
            traverseRecursive(joinDataMap, joinSpecs, createAlias)
        }
        return FromSpec(
            table = rootEntity.dbMapping.table,
            alias = rootAlias,
            joins = joinSpecs
        )
    }

    private fun traverseRecursive(
        joinDataMap: MutableMap<String, JoinData>,
        joinSpecs: MutableList<JoinSpec>,
        createAlias: CreateAlias
    ) {
        joinDataMap.forEach { (_, joinData) ->
            joinSpecs.addAll(createJoinSpecs(joinData, createAlias))
            traverseRecursive(joinData.joinDataMap, joinSpecs, createAlias)
        }
    }

    private fun createJoinSpecs(joinData: JoinData, createAlias: CreateAlias): Collection<JoinSpec> {
        val field = helper.getField(joinData.parentEntity, joinData.childEntityField)
        val relationship = field.relationship
            ?: throw QueryParserException("Relation does not exists in field '${joinData.parentEntity.name}.${joinData.childEntityField}'")
        assertOrThrow(relationship.entity == joinData.childEntity.name) {
            QueryParserException("Assertion failed: Relationship.entity '${relationship.entity}' and joinData.childEntity '${joinData.childEntity.name}' must be same")
        }
        val relationMapping = helper.getRelationMapping(joinData.parentEntity, joinData.childEntityField)

        return when (relationMapping) {
            is DirectRelationMapping -> directRelationMapping(relationMapping, joinData)
            is IndirectRelationMapping -> indirectRelationMapping(relationMapping, joinData, createAlias)
            is VirtualRelationMapping -> virtualRelationMapping(relationMapping, joinData)
            else -> throw QueryParserException("Invalid relationMapping type '${relationMapping::class.simpleName}' in '${joinData.parentEntity}.${joinData.childEntityField}'")
        }
    }

    private fun virtualRelationMapping(relationMapping: VirtualRelationMapping, joinData: JoinData): Collection<JoinSpec> {
        val joinRules = relationMapping.foreignColumnMappingList.map {
            JoinRule(
                from = ColumnSpec(
                    alias = joinData.childEntityAlias, column = it.srcColumn
                ),
                to = ColumnSpec(
                    alias = joinData.parentEntityAlias, column = it.dstColumn
                )
            )
        }
        return listOf(
            JoinSpec(
                joinType = joinData.joinType ?: relationMapping.joinType,
                table = joinData.childEntity.dbMapping.table,
                alias = joinData.childEntityAlias,
                joinRules = joinRules
            )
        )
    }

    private fun directRelationMapping(relationMapping: DirectRelationMapping, joinData: JoinData): Collection<JoinSpec> {

        val joinRules = relationMapping.foreignColumnMappingList.map { JoinRule(
            from = ColumnSpec(alias = joinData.parentEntityAlias, column = it.srcColumn),
            to = ColumnSpec(alias = joinData.childEntityAlias, column = it.dstColumn)
        ) }

        return listOf(
            JoinSpec(
                joinType = joinData.joinType ?: relationMapping.joinType,
                table = joinData.childEntity.dbMapping.table,
                alias = joinData.childEntityAlias,
                joinRules = joinRules
            )
        )
    }

    private fun indirectRelationMapping(
        relationMapping: IndirectRelationMapping,
        joinData: JoinData,
        createAlias: CreateAlias
    ): Collection<JoinSpec> {

        val relationTableAlias = createAlias.create(RELATION_TABLE_ALIAS)

        val srcJoinRules = relationMapping.srcForeignColumnMappingList.map {
            JoinRule(
                from = ColumnSpec(alias = relationTableAlias, column = it.srcColumn),
                to = ColumnSpec(alias = joinData.parentEntityAlias, column = it.dstColumn)
            )
        }

        val dstJoinRules = relationMapping.dstForeignColumnMappingList.map {
            JoinRule(
                from = ColumnSpec(alias = relationTableAlias, column = it.srcColumn),
                to = ColumnSpec(alias = joinData.childEntityAlias, column = it.dstColumn)
            )
        }

        return listOf(
            JoinSpec(
                joinType = joinData.joinType ?: relationMapping.srcJoinType,
                table = joinData.parentEntity.dbMapping.table,
                alias = joinData.parentEntityAlias,
                joinRules = srcJoinRules
            ),
            JoinSpec(
                joinType = joinData.joinType ?: relationMapping.dstJoinType,
                table = joinData.childEntity.dbMapping.table,
                alias = joinData.childEntityAlias,
                joinRules = dstJoinRules
            )
        )
    }
}

private fun makeCreateRelationAlias(): CreateAlias {
    var count = 1;
    return CreateAlias {
        "$it${count++}"
    }
}