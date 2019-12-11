package tai.orm

import tai.base.JsonList
import tai.base.JsonMap

interface QueryExecutor {

    suspend fun query(param: QueryParam): List<JsonMap>

    suspend fun query(param: QueryParam, countKey: String): DataAndCount<JsonMap>

    suspend fun queryForArrays(param: QueryArrayParam): List<JsonList>

    suspend fun queryForArrays(param: QueryArrayParam, countKey: String): DataAndCount<JsonList>

    suspend fun queryForObjects(param: QueryArrayParam): List<JsonMap>

    suspend fun queryForObjects(param: QueryArrayParam, countKey: String): DataAndCount<JsonMap>
}