package tai.sql

import io.reactivex.Single
import tai.base.JsonList
import tai.base.JsonMap
import java.util.stream.Stream

/**
 * Created by sohan on 6/26/2017.
 */
interface BaseSqlDB {

    suspend fun query(sqlQuery: SqlQuery): ResultSet

    suspend fun <T> querySingle(sqlQuery: SqlQuery): T

    suspend fun queryArrays(sqlQuery: SqlQuery): List<JsonList>

    suspend fun queryObjects(sqlQuery: SqlQuery): List<JsonMap>

    suspend fun selectInto(selectInto: SqlSelectIntoOp): UpdateResult;

    suspend fun insertInto(insertInto: SqlInsertIntoOp): UpdateResult;

    suspend fun insert(insert: SqlInsert): UpdateResult

    suspend fun update(updateOp: SqlUpdateOp): UpdateResult

    suspend fun delete(deleteOp: SqlDeleteOp): UpdateResult

    suspend fun execute(operation: SqlOperation): UpdateResult;

    suspend fun executeAll(sqlList: Stream<SqlOperation>): List<UpdateResult>
}
