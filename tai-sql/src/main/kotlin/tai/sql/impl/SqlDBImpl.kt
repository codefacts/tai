package tai.sql.impl

import tai.base.JsonMap
import tai.sql.*

class SqlDBImpl(val baseSqlDB: BaseSqlDB) : SqlDB {

    override suspend fun query(query: SqlQuery): ResultSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insert(table: String, jsonObject: JsonMap) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insert(table: String, sqlList: Collection<JsonMap>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(table: String, data: JsonMap, where: JsonMap): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(table: String, where: JsonMap): Int {
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

    override suspend fun updateAll(sqlList: Collection<SqlOperation>): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}