package tai.orm.query.impl

import tai.base.JsonMap
import tai.base.PrimitiveValue
import tai.criteria.operators.arg_
import tai.criteria.operators.op_
import tai.orm.*
import tai.orm.core.FieldExpression
import tai.orm.entity.Entity
import tai.orm.entity.EntityMappingHelper
import tai.orm.query.AliasAndColumn
import tai.orm.query.QueryExecutor
import tai.orm.query.ex.QueryParserException
import tai.sql.*

class QueryExecutorImpl(val helper: EntityMappingHelper) : QueryExecutor {
    val joinDataHelper = JoinDataHelper(helper)

    override suspend fun findAll(param: QueryParam): List<JsonMap> {
        return doFindAll(param)
    }

    private fun doFindAll(param: QueryParam): List<JsonMap> {
        val sqlQuery: SqlQuery = toSqlQuery(param)
        return emptyList()
    }

    private fun toSqlQuery(param: QueryParam): SqlQuery {
        val rootAlias = param.alias;
        val rootEntity = helper.getEntity(param.entity)

        val aliasToJoinDataMap = mutableMapOf<String, MutableMap<String, JoinData>>() //alias -> (field -> joinData)
        val aliasToEntityMap = mutableMapOf<String, tai.orm.entity.Entity>()

        val aliasToJoinParamMap = param.joinParams.asSequence().map { it.alias to it }.toMap()
        val aliasToFullPathExpMap = createAliasToFullPathExpMap(
            param.alias,
            param.joinParams,
            aliasToJoinParamMap
        )

        val rootJoinDataMap = mutableMapOf<String, JoinData>()

        aliasToEntityMap[rootAlias] = rootEntity
        aliasToJoinDataMap[rootAlias] = rootJoinDataMap

        val createAlias = makeCreateAlias()

        aliasToFullPathExpMap.forEach {(alias, fullPathExp) ->

            val (childAlias, childEntity) = joinDataHelper.populateJoinDataMap(
                rootAlias, rootEntity, fullPathExp, joinParam = aliasToJoinParamMap[alias] ?: throw QueryParserException("No JoinParam found in aliasToJoinParamMap for alias '$alias' -> '$fullPathExp'"),
                rootJoinDataMap = rootJoinDataMap, createAlias = {shortCode, isLast -> if (isLast) alias else createAlias(shortCode) }
            )

            aliasToEntityMap[childAlias] = childEntity
            aliasToJoinDataMap[childAlias] = mutableMapOf()
        }

        val where = translateAll(param.criteria, aliasToEntityMap, aliasToJoinDataMap, createAlias)
        val groupBy = translateAll(param.groupBy, aliasToEntityMap, aliasToJoinDataMap, createAlias)
        val having = translateAll(param.having, aliasToEntityMap, aliasToJoinDataMap, createAlias)
        val selections = translateSelections(
            param.selections, aliasToEntityMap, aliasToJoinDataMap, createAlias
        )
        val orderBy = translateOrderBy(param.orderBy, aliasToEntityMap, aliasToJoinDataMap, createAlias)
        val pagination = param.pagination?.let { translatePagination(it, aliasToEntityMap, aliasToJoinDataMap, createAlias) }

        val from = JoinDataToJoinSpecBuilder(
            helper, rootEntity, rootAlias, fullPathExpToJoinParamMap = aliasToFullPathExpMap.entries.map { (alias, pathExp) ->
                pathExp to (aliasToJoinParamMap[alias] ?: throw QueryParserException("No join param found in aliasToJoinParamMap for alias '$alias' -> '$pathExp'"))
            }.toMap()
            ).translateFrom(
            aliasToJoinDataMap
        )

        val sqlQuery = SqlQuery(
            selections = selections,
            from = listOf(from),
            where = where,
            groupBy = groupBy,
            having = having,
            orderBy = orderBy,
            pagination = pagination
        )
        return sqlQuery
    }

    private fun translatePagination(
        pagination: Pagination,
        aliasToEntityMap: MutableMap<String, Entity>,
        aliasToJoinDataMap: MutableMap<String, MutableMap<String, JoinData>>,
        createAlias: CreateAlias
    ): SqlPagination {
        val aliasAndColumn = translateToAliasAndColumn(pagination.fieldExpression, aliasToEntityMap, aliasToJoinDataMap, createAlias)
        return SqlPagination(
            ColumnSpec(aliasAndColumn.alias, aliasAndColumn.column),
            pagination.offset, pagination.size
        )
    }

    private fun translateOrderBy(
        orderBy: List<OrderByData>,
        aliasToEntityMap: MutableMap<String, Entity>,
        aliasToJoinDataMap: MutableMap<String, MutableMap<String, JoinData>>,
        createAlias: CreateAlias
    ): List<OrderBySpec> {
        return orderBy.map {
            val translatedColumnExp = translate(
                it.fieldExpression, aliasToEntityMap, aliasToJoinDataMap, createAlias
            )
            OrderBySpec(translatedColumnExp, it.order)
        }
    }

    private fun translateSelections(
        selections: Collection<FieldExpression>,
        aliasToEntityMap: MutableMap<String, Entity>,
        aliasToJoinDataMap: MutableMap<String, MutableMap<String, JoinData>>,
        createAlias: CreateAlias
    ): Collection<JsonMap> {
        return selections.map { fieldExp ->

            val aliasAndColumn = translateToAliasAndColumn(
                fieldExp, aliasToEntityMap, aliasToJoinDataMap, createAlias
            )

            return@map createdTranslatedField(fieldExp, aliasAndColumn)
        }
    }

    private fun translateToAliasAndColumn(
        fieldExp: FieldExpression,
        aliasToEntityMap: MutableMap<String, Entity>,
        aliasToJoinDataMap: MutableMap<String, MutableMap<String, JoinData>>,
        createAlias: CreateAlias
    ): AliasAndColumn {

        val pathExp = fieldExp.parent
        val alias = pathExp.root()
        val entity = aliasToEntityMap[alias] ?: throw QueryParserException("Field Expression '$fieldExp' starts with an invalid alias")
        val rootJoinDataMap = aliasToJoinDataMap[alias] ?: throw OrmException("No JoinData found for alias '$alias' in pathExpression '$pathExp'")

        val (lastAlias, lastEntity) = joinDataHelper.populateJoinDataMap(
            rootAlias = alias, rootEntity = entity, fullPathExp = pathExp,
            joinParam = null, rootJoinDataMap = rootJoinDataMap, createAlias = { shortCode, isLast -> createAlias(shortCode) }
        )
        return AliasAndColumn(
            lastAlias,
            helper.getColumnMapping(entity, fieldExp.field).column
        )
    }

    private fun createdTranslatedField(fieldExp: FieldExpression, aliasAndColumn: AliasAndColumn): JsonMap {
        return field(fieldExp) + translateToAliasAndColumn(aliasAndColumn)
    }

    private fun translateToAliasAndColumn(aliasAndColumn: AliasAndColumn): JsonMap {
        return mapOf(
            translatedTo_ to aliasAndColumn
        )
    }

    private fun translateAll(
        criteria: List<JsonMap>,
        aliasToEntityMap: MutableMap<String, Entity>,
        aliasToJoinDataMap: MutableMap<String, MutableMap<String, JoinData>>,
        createAlias: CreateAlias
    ): List<JsonMap> {
        return criteria.map { translate(
            it, aliasToEntityMap, aliasToJoinDataMap, createAlias
        ) }
    }

    private fun translate(
        json: JsonMap,
        aliasToEntityMap: MutableMap<String, Entity>,
        aliasToJoinDataMap: MutableMap<String, MutableMap<String, JoinData>>,
        createAlias: CreateAlias
    ): JsonMap {

        return traverseFieldsInJsonMap(json) {
            val fieldExp = it[arg_] as FieldExpression

            val aliasAndColumn = translateToAliasAndColumn(fieldExp, aliasToEntityMap, aliasToJoinDataMap, createAlias)

            return@traverseFieldsInJsonMap it + translateToAliasAndColumn(aliasAndColumn)
        }
    }

    override suspend fun findAll(param: QueryParam, countKey: String): DataAndCount<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun queryForDataGrid(param: QueryArrayParam): DataGrid {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun queryForDataGrid(param: QueryArrayParam, countKey: String): DataGridAndCount {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun queryForObjects(param: QueryArrayParam): List<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun queryForObjects(param: QueryArrayParam, countKey: String): DataAndCount<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        fun traverseFieldsInJsonMap(json: JsonMap, fieldHandler: (JsonMap) -> JsonMap): JsonMap {
            return json.entries.map { (key, value) -> key to processValue(
                value,
                fieldHandler
            )
            }.toMap()
        }

        private fun processValue(value: PrimitiveValue?, fieldHandler: (JsonMap) -> JsonMap): PrimitiveValue? {
            if (value == null) {
                return null
            }
            if (value is Map<*, *>) {
                if (value[op_] == field_) {
                    return fieldHandler(value as JsonMap)
                }
            }
            if (value is List<*>) {
                return value.map {
                    processValue(
                        value,
                        fieldHandler
                    )
                }
            }
            return value
        }

    }
}