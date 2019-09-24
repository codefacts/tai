package tai.sql

import io.reactivex.Single

/**
 * Created by sohan on 6/26/2017.
 */
interface BaseSqlDB {

    suspend fun query(sqlQuery: SqlQuery): ResultSet

    suspend fun update(sqlList: Collection<SqlUpdate>): Int
}
