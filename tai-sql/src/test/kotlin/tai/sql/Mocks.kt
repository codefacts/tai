package tai.sql

import tai.base.JsonList
import tai.criteria.SqlAndParams
import tai.sql.impl.ResultSetImpl

class SqlExecutorMock : SqlExecutor {

    override suspend fun query(sql: String): ResultSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun query(sql: String, params: JsonList): ResultSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun <T> queryScalar(sql: String): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun <T> queryScalar(sql: String, params: JsonList): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun execute(sql: String): UpdateResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun execute(sql: String, params: JsonList): UpdateResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun executeALL(sqlList: Collection<String>): List<UpdateResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun executeAll(sqlUpdates: Collection<SqlAndParams>): List<UpdateResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}