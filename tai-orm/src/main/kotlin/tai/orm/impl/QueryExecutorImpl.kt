package tai.orm.impl

import tai.base.JsonMap
import tai.orm.*

class QueryExecutorImpl : QueryExecutor {
    override suspend fun findAll(param: QueryParam): List<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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