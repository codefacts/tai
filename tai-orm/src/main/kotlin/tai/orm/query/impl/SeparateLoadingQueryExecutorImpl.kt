package tai.orm.query.impl

import tai.base.JsonMap
import tai.orm.DataAndCount
import tai.orm.QueryParam
import tai.orm.core.FieldExpression
import tai.orm.query.QueryExecutor

class SeparateLoadingQueryExecutorImpl(val queryExecutor: QueryExecutor) : QueryExecutor by queryExecutor {

    override suspend fun findAll(param: QueryParam): List<JsonMap> {

    }

    override suspend fun findAllWithCount(param: QueryParam): DataAndCount<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun findAllWithCount(param: QueryParam, countKey: FieldExpression): DataAndCount<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}