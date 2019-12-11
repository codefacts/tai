package tai.orm

import tai.base.JsonList
import tai.base.JsonMap

interface BaseOrm {

    suspend fun upsert(param: UpsertParam): UpsertParam

    suspend fun delete(param: DeleteParam)

    suspend fun execute(params: Collection<ExecuteParam>): Collection<ExecuteParam>

    suspend fun query(param: QueryParam): List<JsonMap>

    suspend fun query(param: QueryParam, countKey: String): DataAndCount<JsonMap>

    suspend fun queryForArrays(param: QueryArrayParam): List<JsonList>

    suspend fun queryForArrays(param: QueryArrayParam, countKey: String): DataAndCount<JsonList>

    suspend fun queryForObjects(param: QueryArrayParam): List<JsonMap>

    suspend fun queryForObjects(param: QueryArrayParam, countKey: String): DataAndCount<JsonMap>
}