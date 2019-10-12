package tai.sql.impl

import tai.base.JsonMap
import tai.criteria.ops.*
import tai.sql.*

class SqlDBImpl(val baseSqlDB: BaseSqlDB) : SqlDB, BaseSqlDB by baseSqlDB {

    override suspend fun query(query: SqlQuery): ResultSet {
        return baseSqlDB.query(query);
    }

    override suspend fun insert(table: String, jsonObject: JsonMap): UpdateResult {
        return baseSqlDB.execute(
            SqlInsert(
                table, jsonObject
            )
        )
    }

    override suspend fun insert(table: String, sqlList: Collection<JsonMap>): List<UpdateResult> {
        return baseSqlDB.executeAll(
            sqlList.map { jo -> SqlInsert(table, jo) }
        )
    }

    override suspend fun update(table: String, data: JsonMap, where: JsonMap): UpdateResult {
        return baseSqlDB.update(
            SqlUpdateOp(
                table = table,
                values = data.entries.map { (key, value) ->
                    ColumnAndValue(
                        key, if (value != null) valueOf(value) else nullValue()
                    )
                },
                where = where.map { (key, value) ->
                    if (value != null) eq(column(key), valueOf(value)) else isNull(
                        column(key)
                    )
                }
            )
        )
    }

    override suspend fun delete(table: String, where: JsonMap): UpdateResult {
        return baseSqlDB.delete(
            SqlDeleteOp(
                table = table,
                where = where.map { (key, value) ->
                    if (value != null) eq(column(key), valueOf(value)) else isNull(
                        column(key)
                    )
                }
            )
        )
    }
}