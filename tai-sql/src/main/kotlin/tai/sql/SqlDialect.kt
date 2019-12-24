package tai.sql

import tai.base.JsonMap
import tai.sql.SqlInsertIntoOp
import tai.sql.SqlQuery
import tai.sql.SqlSelectIntoOp

interface SqlDialect {
    suspend fun executePaginated(sqlQuery: SqlQuery): ResultSet
    suspend fun executePaginated(sqlQuery: SqlSelectIntoOp): UpdateResult
    suspend fun executePaginated(insertInto: SqlInsertIntoOp): UpdateResult
}