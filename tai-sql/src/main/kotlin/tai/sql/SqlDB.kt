package tai.sql

import io.reactivex.Completable
import io.reactivex.Single
import tai.base.JsonMap
import java.util.stream.Stream

/**
 * Created by Jango on 9/25/2016.
 */
interface SqlDB {

    suspend fun query(query: SqlQuery): ResultSet

    suspend fun insert(table: String, jsonObject: JsonMap)

    suspend fun insert(table: String, sqlList: Collection<JsonMap>)

    suspend fun update(table: String, data: JsonMap, where: JsonMap): Int

    suspend fun delete(table: String, where: JsonMap): Int

    suspend fun selectInto(selectInto: SqlSelectIntoOp): Int;

    suspend fun insertInto(selectInto: SqlInsertIntoOp): Int;

    suspend fun update(update: SqlUpdateOp): Int

    suspend fun delete(update: SqlDeleteOp): Int

    suspend fun updateAll(sqlList: Collection<SqlOperation>): Int
}
