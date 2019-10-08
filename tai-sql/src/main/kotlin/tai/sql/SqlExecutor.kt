package tai.sql

import tai.base.JsonList

/**
 * Created by sohan on 3/14/2017.
 */
interface SqlExecutor {

    suspend fun query(sql: String): ResultSet

    suspend fun query(sql: String, params: JsonList): ResultSet

    suspend fun <T> queryScalar(sql: String): T

    suspend fun <T> queryScalar(sql: String, params: JsonList): T

    suspend fun update(sql: String): Int

    suspend fun update(sql: String, params: JsonList): Int

    suspend fun updateAll(sqlList: List<String>): Int

    suspend fun updateAll(sqlUpdates: Collection<SqlAndParams>): List<Int>
}

data class SqlAndParams(
    val sql: String,
    val params: JsonList
);
