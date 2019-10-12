package tai.sql

import tai.base.JsonMap
import tai.sql.SqlInsertIntoOp
import tai.sql.SqlQuery
import tai.sql.SqlSelectIntoOp

interface SqlDialect {
    fun toPaginatedQuery(sqlQuery: SqlQuery): JsonMap
    fun toPaginatedQuery(sqlQuery: SqlSelectIntoOp): JsonMap;
    fun toPaginatedQuery(insertInto: SqlInsertIntoOp): JsonMap
}