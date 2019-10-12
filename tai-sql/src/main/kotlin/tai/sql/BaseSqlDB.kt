package tai.sql

import io.reactivex.Single

/**
 * Created by sohan on 6/26/2017.
 */
interface BaseSqlDB {

    suspend fun query(sqlQuery: SqlQuery): ResultSet

    suspend fun selectInto(selectInto: SqlSelectIntoOp): UpdateResult;

    suspend fun insertInto(insertInto: SqlInsertIntoOp): UpdateResult;

    suspend fun insert(insert: SqlInsert): UpdateResult

    suspend fun update(updateOp: SqlUpdateOp): UpdateResult

    suspend fun delete(deleteOp: SqlDeleteOp): UpdateResult

    suspend fun execute(operation: SqlOperation): UpdateResult;

    suspend fun executeAll(sqlList: Collection<SqlOperation>): List<UpdateResult>
}
