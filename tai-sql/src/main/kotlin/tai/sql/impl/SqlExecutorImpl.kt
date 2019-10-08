package tai.sql.impl

import tai.base.JsonList
import tai.sql.ResultSet
import tai.sql.SqlAndParams
import tai.sql.SqlExecutor

class SqlExecutorImpl : SqlExecutor {
    
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

    override suspend fun update(sql: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(sql: String, params: JsonList): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateAll(sqlList: List<String>): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateAll(sqlUpdates: Collection<SqlAndParams>): List<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}