package tai.sql

import io.reactivex.Completable
import io.reactivex.Single
import tai.base.JsonMap
import tai.criteria.CriOperation
import java.util.stream.Stream

/**
 * Created by Jango on 9/25/2016.
 */
interface SqlDB {

    suspend fun query(query: SqlQuery): ResultSet

    suspend fun insert(table: String, jsonObject: JsonMap)

    suspend fun insert(table: String, sqlList: Collection<JsonMap>)

    suspend fun update(table: String, data: JoinData, where: Collection<CriOperation>): Int

    suspend fun delete(table: String, where: Collection<CriOperation>): Int

    suspend fun update(sqlList: Collection<SqlOperation>): Int
}
