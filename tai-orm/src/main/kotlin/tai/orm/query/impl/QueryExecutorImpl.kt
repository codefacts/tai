package tai.orm.query.impl

import tai.base.JsonMap
import tai.criteria.ops.column
import tai.criteria.ops.count
import tai.criteria.ops.distinct
import tai.orm.*
import tai.orm.core.FieldExpression
import tai.orm.entity.EntityMappingHelper
import tai.orm.query.AliasAndColumn
import tai.orm.query.QueryExecutor
import tai.orm.query.ex.MultipleResultException
import tai.orm.query.ex.NoResultException
import tai.orm.read.ConvertToObjects
import tai.orm.read.makeConvertToObjects
import tai.orm.read.makeReadObject
import tai.sql.*
import java.lang.NullPointerException

class QueryExecutorImpl(val helper: EntityMappingHelper, val baseSqlDB: BaseSqlDB) : QueryExecutor {
    val parser = QueryParser(helper)

    override suspend fun findAll(param: QueryParam): List<JsonMap> {
        return doFindAll(param)
    }

    override suspend fun findAllWithCount(param: QueryParam): DataAndCount<JsonMap> {
        return findAllWithCount(param, FieldExpression.create(param.alias, helper.getPrimaryKey(param.entity)))
    }

    override suspend fun findAllWithCount(param: QueryParam, countKey: FieldExpression): DataAndCount<JsonMap> {
        return doFindAll(param, countKey)
    }

    override suspend fun query(param: QueryArrayParam): DataGrid {
        return doQuery(param)
    }

    override suspend fun query(param: QueryArrayParam, countKey: FieldExpression): DataGridAndCount {
        return doQuery(param, countKey)
    }

    override suspend fun <T> querySingle(param: QueryArrayParam): T {
        val data = query(param).data
        if (data.isEmpty()) {
            throw NoResultException("No result found in query single, expected exactly one result")
        }
        if (data.size > 1) {
            throw MultipleResultException("Multiple result found in query single, expected exactly one result")
        }
        return data[0][0] as T
    }

    override suspend fun queryObjects(param: QueryArrayParam): List<JsonMap> {
        return doQueryObjects(param)
    }

    override suspend fun queryObjects(param: QueryArrayParam, countKey: FieldExpression): DataAndCount<JsonMap> {
        return doQueryObjects(param, countKey)
    }

    private suspend fun doFindAll(param: QueryParam, countKey: FieldExpression): DataAndCount<JsonMap> {

        val (sqlQuery, countKeyAliasAndColumn, convertToObjects) = toQueryElements(param, countKey)

        if (countKeyAliasAndColumn == null) {
            throw NullPointerException("CountKey '$countKey' translation failed for entity '${param.entity}'")
        }

        val dataList = baseSqlDB.queryArrays(sqlQuery)
        val count = baseSqlDB.querySingle<Long>(
            sqlQuery.copy(
                selections = listOf(
                    count(
                        distinct(column(countKeyAliasAndColumn.alias, countKeyAliasAndColumn.column))
                    )
                ),
                orderBy = listOf(),
                pagination = null
            )
        )
        val data = convertToObjects(dataList)
        return DataAndCount(data, count)
    }

    private suspend fun doFindAll(param: QueryParam): List<JsonMap> {

        val (sqlQuery, _, convertToObjects) = toQueryElements(param, null)
        val dataList = baseSqlDB.queryArrays(sqlQuery)
        return convertToObjects(dataList)
    }

    private suspend fun doQuery(param: QueryArrayParam): DataGrid {

        val (sqlQuery, _) = toQueryElements(param, null)

        val resultSet = baseSqlDB.query(sqlQuery)
        return DataGrid(
            columns = resultSet.columnNames,
            data = resultSet.results
        )
    }

    private suspend fun doQuery(param: QueryArrayParam, countKey: FieldExpression): DataGridAndCount {

        val (sqlQuery, countKeyAliasAndColumn) = toQueryElements(param, countKey)

        if (countKeyAliasAndColumn == null) {
            throw NullPointerException("CountKey '$countKey' translation failed for entity '${param.entity}'")
        }

        val resultSet = baseSqlDB.query(sqlQuery)
        val count = baseSqlDB.querySingle<Long>(createCountQuery(sqlQuery, countKeyAliasAndColumn))

        return DataGridAndCount(
            columns = resultSet.columnNames,
            data = resultSet.results,
            count = count
        )
    }

    private suspend fun doQueryObjects(param: QueryArrayParam): List<JsonMap> {
        val (sqlQuery, _) = toQueryElements(param, null)
        return baseSqlDB.queryObjects(sqlQuery)
    }

    private suspend fun doQueryObjects(param: QueryArrayParam, countKey: FieldExpression): DataAndCount<JsonMap> {

        val (sqlQuery, countKeyAliasAndColumn) = toQueryElements(param, countKey)

        if (countKeyAliasAndColumn == null) {
            throw NullPointerException("CountKey '$countKey' translation failed for entity '${param.entity}'")
        }

        val data = baseSqlDB.queryObjects(sqlQuery)
        val count = baseSqlDB.querySingle<Long>(createCountQuery(sqlQuery, countKeyAliasAndColumn))

        return DataAndCount(data, count)
    }

    private fun toQueryElements(param: QueryParam, countKey: FieldExpression?): QueryElements {
        val aliasToJoinParamMap = createAliasToJoinParamMap(param.joinParams)
        val aliasToFullPathExpMap = createAliasToFullPathExpMap(
            param.alias,
            param.joinParams,
            aliasToJoinParamMap
        )

        val (sqlQuery, countKeyAliasAndColumn) = parser.translateQueryParam(param, countKey, aliasToJoinParamMap, aliasToFullPathExpMap)

        val readObject = makeReadObject(
            fieldExpressionToIndexMap = param.selections.asSequence().mapIndexed { index, exp ->
                exp to index
            }.toMap(),
            rootAlias = param.alias,
            rootEntity = param.entity,
            helper = helper,
            aliasToFullPathExpressionMap = aliasToFullPathExpMap
        )

        val convertToObjects = makeConvertToObjects(readObject, helper.getPrimaryKey(param.entity))

        return QueryElements(sqlQuery, countKeyAliasAndColumn, convertToObjects)
    }

    private fun toQueryElements(param: QueryArrayParam, countKey: FieldExpression?): QueryParser.TranslationResult {
        val aliasToJoinParamMap = createAliasToJoinParamMap(param.joinParams)
        val aliasToFullPathExpMap = createAliasToFullPathExpMap(
            param.alias,
            param.joinParams,
            aliasToJoinParamMap
        )

        return parser.translateQueryArrayParam(param, countKey, aliasToJoinParamMap, aliasToFullPathExpMap)
    }

    private fun createCountQuery(sqlQuery: SqlQuery, countKeyAliasAndColumn: AliasAndColumn): SqlQuery {
        return sqlQuery.copy(
            selections = listOf(
                count(
                    distinct(column(countKeyAliasAndColumn.alias, countKeyAliasAndColumn.column))
                )
            ),
            orderBy = listOf(),
            pagination = null
        )
    }

    data class QueryElements(val sqlQuery: SqlQuery, val countKeyAliasAndColumn: AliasAndColumn?, val convertToObjects: ConvertToObjects)
}