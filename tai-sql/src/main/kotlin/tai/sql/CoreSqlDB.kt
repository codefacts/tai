package tai.sql

import tai.base.JsonMap

interface CoreSqlDB {

    suspend fun query(query: JsonMap): ResultSet;

    suspend fun execute(operation: JsonMap): UpdateResult;

    suspend fun executeAll(operations: List<JsonMap>): List<UpdateResult>;
}