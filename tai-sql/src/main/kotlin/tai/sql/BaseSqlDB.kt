package tai.sql

import io.reactivex.Single

/**
 * Created by sohan on 6/26/2017.
 */
interface BaseSqlDB {

    suspend fun query(sqlQuery: SqlQuery): ResultSet

    suspend fun selectInto(selectInto: SqlSelectIntoOp): Int;

    suspend fun insertInto(selectInto: SqlInsertIntoOp): Int;

    suspend fun update(update: SqlUpdateOp): Int

    suspend fun delete(update: SqlDeleteOp): Int

    suspend fun updateAll(sqlList: Collection<SqlUpdate>): Int
}
