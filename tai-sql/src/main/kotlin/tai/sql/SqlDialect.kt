package tai.sql

import tai.base.JsonMap
import tai.sql.SqlInsertIntoOp
import tai.sql.SqlQuery
import tai.sql.SqlSelectIntoOp

interface SqlDialect {
    fun toExecutePaginated(sqlQuery: SqlQuery): JsonMap
    fun toExecutePaginated(sqlQuery: SqlSelectIntoOp): JsonMap;
    fun toExecutePaginated(insertInto: SqlInsertIntoOp): JsonMap
}