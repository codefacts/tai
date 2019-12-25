package tai.sql.impl

import tai.sql.*

class MySql8DialectImpl: SqlDialect {
    override suspend fun executePaginated(sqlQuery: SqlQuery): ResultSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun executePaginated(sqlQuery: SqlSelectIntoOp): UpdateResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun executePaginated(insertInto: SqlInsertIntoOp): UpdateResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}