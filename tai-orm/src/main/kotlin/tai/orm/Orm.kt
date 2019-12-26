package tai.orm

import tai.base.JsonMap
import tai.orm.core.FieldExpression

interface Orm {

    suspend fun countDistinct(entity: String): Long

    suspend fun countDistinct(param: CountDistinctParam): Long

    suspend fun <T> findOne(param: QueryParam): JsonMap

    suspend fun findAll(param: QueryParam): List<JsonMap>

    suspend fun findAll(param: QueryParam, countKey: FieldExpression): DataAndCount<JsonMap>

    suspend fun queryForDataGrid(param: QueryArrayParam): DataGrid

    suspend fun queryForDataGrid(param: QueryArrayParam, countKey: FieldExpression): DataGridAndCount

    suspend fun queryForObjects(param: QueryArrayParam): List<JsonMap>

    suspend fun queryForObjects(param: QueryArrayParam, countKey: FieldExpression): DataAndCount<JsonMap>

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