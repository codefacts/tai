package tai.sql.impl

import tai.base.JsonMap
import tai.sql.CoreSqlDB
import tai.sql.SqlExecutor

class CoreSqlDBImpl(val sqlExecutor: SqlExecutor) : CoreSqlDB {

    override suspend fun query(query: JsonMap) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(operation: JsonMap) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateAll(operations: List<JsonMap>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}