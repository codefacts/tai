package tai.orm.query.impl

import tai.base.JsonMap
import tai.criteria.ops.column
import tai.criteria.ops.count
import tai.criteria.ops.distinct
import tai.orm.*
import tai.orm.core.FieldExpression
import tai.orm.entity.EntityMappingHelper
import tai.orm.query.QueryExecutor
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun queryObjects(param: QueryArrayParam): List<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun queryObjects(param: QueryArrayParam, countKey: FieldExpression): DataAndCount<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private suspend fun doFindAll(param: QueryParam, countKey: FieldExpression): DataAndCount<JsonMap> {

        val aliasToJoinParamMap = createAliasToJoinParamMap(param.joinParams)
        val aliasToFullPathExpMap = createAliasToFullPathExpMap(
            param.alias,
            param.joinParams,
            aliasToJoinParamMap
        )

        val (sqlQuery, countKeyAliasAndColumn) = parser.translateQueryParam(param, countKey, aliasToJoinParamMap, aliasToFullPathExpMap)

        if (countKeyAliasAndColumn == null) {
            throw NullPointerException("CountKey '$countKey' translation failed for entity '${param.entity}'")
        }

        val readObject = makeReadObject(
            fieldExpressionToIndexMap = param.selections.asSequence().mapIndexed { index, exp ->
                exp to index
            }.toMap(),
            rootAlias = param.alias,
            rootEntity = param.entity,
            helper = helper,
            aliasToFullPathExpressionMap = aliasToFullPathExpMap
        )

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
        val data = dataList.map { readObject(it, dataList) }
        return DataAndCount(data, count)
    }

    private suspend fun doFindAll(param: QueryParam): List<JsonMap> {

        val aliasToJoinParamMap = createAliasToJoinParamMap(param.joinParams)
        val aliasToFullPathExpMap = createAliasToFullPathExpMap(
            param.alias,
            param.joinParams,
            aliasToJoinParamMap
        )

        val (sqlQuery, _) = parser.translateQueryParam(param, null, aliasToJoinParamMap, aliasToFullPathExpMap)

        val readObject = makeReadObject(
            fieldExpressionToIndexMap = param.selections.asSequence().mapIndexed { index, exp ->
                exp to index
            }.toMap(),
            rootAlias = param.alias,
            rootEntity = param.entity,
            helper = helper,
            aliasToFullPathExpressionMap = aliasToFullPathExpMap
        )

        val dataList = baseSqlDB.queryArrays(sqlQuery)
        return dataList.map { readObject(it, dataList) }
    }

    private fun doQuery(param: QueryArrayParam): DataGrid {

        val aliasToJoinParamMap = createAliasToJoinParamMap(param.joinParams)
        val aliasToFullPathExpMap = createAliasToFullPathExpMap(
            param.alias,
            param.joinParams,
            aliasToJoinParamMap
        )

        val (sqlQuery, _) = parser.translateQueryParam(param, null, aliasToJoinParamMap, aliasToFullPathExpMap)

        val readObject = makeReadObject(
            fieldExpressionToIndexMap = param.selections.asSequence().mapIndexed { index, exp ->
                exp to index
            }.toMap(),
            rootAlias = param.alias,
            rootEntity = param.entity,
            helper = helper,
            aliasToFullPathExpressionMap = aliasToFullPathExpMap
        )

        val dataList = baseSqlDB.queryArrays(sqlQuery)

    }
}