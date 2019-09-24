package tai.sql

import io.reactivex.Single
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

    suspend fun update(sqlList: List<String>): Int

    suspend fun update(sqlUpdates: Collection<UpdateData>): List<Int>
}

data class UpdateData(
    val sql: String,
    val params: JsonList
);
