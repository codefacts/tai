package tai.sql

import tai.base.JsonMap

interface CoreSqlDB {

    suspend fun query(query: JsonMap);

    suspend fun update(operation: JsonMap);

    suspend fun updateAll(operations: List<JsonMap>);
}