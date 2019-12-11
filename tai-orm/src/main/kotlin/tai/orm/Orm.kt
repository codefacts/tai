package tai.orm

import tai.base.JsonList
import tai.base.JsonMap

interface Orm {

    suspend fun countDistinct(entity: String): Long

    suspend fun countDistinct(param: CountDistinctParam): Long

    suspend fun <T> findOne(param: QueryParam): JsonMap

    suspend fun findAll(param: QueryParam): List<JsonMap>

    suspend fun findAll(params: QueryParam, countKey: String): DataAndCount<JsonMap>

    suspend fun queryForArrays(param: QueryArrayParam): List<JsonList>

    suspend fun queryForArrays(param: QueryArrayParam, countKey: String): DataAndCount<JsonList>

    suspend fun queryForObjects(param: QueryArrayParam): List<JsonMap>

    suspend fun queryForObjects(param: QueryArrayParam, countKey: String): DataAndCount<JsonMap>

    suspend fun <T> querySingle(param: QueryArrayParam): T

    suspend fun upsert(entity: String, data: JsonMap): JsonMap

    suspend fun upsertAll(entity: String, jsonObjects: List<JsonMap>): List<JsonMap>

    suspend fun <T> delete(entity: String, id: T)

    suspend fun <T> deleteAll(entity: String, ids: List<T>)

    suspend fun <T> deleteEntity(entity: String, obj: JsonMap)

    suspend fun <T> deleteAllEntities(entity: String, objects: List<JsonMap>)

    suspend fun execute(param: ExecuteParam): ExecuteParam

    suspend fun executeAll(params: Collection<ExecuteParam>): Collection<ExecuteParam>
}