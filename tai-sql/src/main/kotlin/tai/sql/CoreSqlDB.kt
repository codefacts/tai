package tai.sql

import tai.base.JsonMap
import java.util.stream.Stream

interface CoreSqlDB {

    suspend fun query(query: JsonMap): ResultSet;

    suspend fun execute(operation: JsonMap): UpdateResult;

    suspend fun executeAll(operations: Stream<JsonMap>): List<UpdateResult>;
}