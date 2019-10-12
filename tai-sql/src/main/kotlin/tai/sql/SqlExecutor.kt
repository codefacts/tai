package tai.sql

import tai.base.JsonList
import tai.criteria.SqlAndParams

/**
 * Created by sohan on 3/14/2017.
 */
interface SqlExecutor {

    suspend fun query(sql: String): ResultSet

    suspend fun query(sql: String, params: JsonList): ResultSet

    suspend fun <T> queryScalar(sql: String): T

    suspend fun <T> queryScalar(sql: String, params: JsonList): T

    suspend fun execute(sql: String): UpdateResult

    suspend fun execute(sql: String, params: JsonList): UpdateResult

    suspend fun executeALL(sqlList: Collection<String>): List<UpdateResult>

    suspend fun executeAll(sqlUpdates: Collection<SqlAndParams>): List<UpdateResult>
}
