package tai.sql

import io.reactivex.Completable
import io.reactivex.Single
import tai.base.JsonMap
import java.util.stream.Stream

/**
 * Created by Jango on 9/25/2016.
 */
interface SqlDB : BaseSqlDB {

    suspend fun insert(table: String, jsonObject: JsonMap): UpdateResult

    suspend fun insert(table: String, sqlList: Stream<JsonMap>): List<UpdateResult>

    suspend fun update(table: String, data: JsonMap, where: JsonMap): UpdateResult

    suspend fun delete(table: String, where: JsonMap): UpdateResult
}
