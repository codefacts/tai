package tai.sql.impl

import tai.sql.*

class BaseSqlDBImpl(val coreSqlDB: CoreSqlDB) : BaseSqlDB {

    override suspend fun query(sqlQuery: SqlQuery): ResultSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectInto(selectInto: SqlSelectIntoOp): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insertInto(selectInto: SqlInsertIntoOp): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(update: SqlUpdateOp): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(update: SqlDeleteOp): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateAll(sqlList: Collection<SqlUpdate>): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}