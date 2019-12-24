package tai.orm.query.impl

import tai.base.JsonMap
import tai.base.PrimitiveValue
import tai.criteria.operators.arg_
import tai.criteria.operators.op_
import tai.orm.*
import tai.orm.core.FieldExpression
import tai.orm.core.PathExpression
import tai.orm.entity.Entity
import tai.orm.entity.EntityMappingHelper
import tai.orm.query.AliasAndColumn
import tai.orm.query.QueryExecutor
import tai.orm.query.ex.QueryParserException
import tai.orm.read.ReadObject
import tai.orm.read.makeReadObject
import tai.sql.*

class QueryExecutorImpl(val helper: EntityMappingHelper, val baseSqlDB: BaseSqlDB) : QueryExecutor {
    val parser = QueryParser(helper)

    override suspend fun findAll(param: QueryParam): List<JsonMap> {
        return doFindAll(param)
    }

    private suspend fun doFindAll(param: QueryParam): List<JsonMap> {

        val aliasToJoinParamMap = createAliasToJoinParamMap(param.joinParams)
        val aliasToFullPathExpMap = createAliasToFullPathExpMap(
            param.alias,
            param.joinParams,
            aliasToJoinParamMap
        )

        val sqlQuery: SqlQuery = parser.toSqlQuery(param, aliasToJoinParamMap, aliasToFullPathExpMap)

        val readObject = makeReadObject(
            fieldExpressionToIndexMap = param.selections.asSequence().mapIndexed { index, exp ->
                exp to index
            }.toMap(),
            rootAlias = param.alias,
            rootEntity = param.entity,
            helper = helper,
            aliasToFullPathExpressionMap = aliasToFullPathExpMap
        )

        val dataList = baseSqlDB.queryForArrays(sqlQuery)
        return dataList.map { readObject(it, dataList) }
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
}