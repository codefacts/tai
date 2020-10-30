package tai.orm

import tai.base.JsonMap
import tai.orm.core.FieldExpression
import java.util.stream.Stream

interface Orm {

    suspend fun countDistinct(entity: String): Long

    suspend fun countDistinct(param: CountDistinctParam): Long

    suspend fun findOne(param: QueryParam): JsonMap

    suspend fun findAll(param: QueryParam): List<JsonMap>

    suspend fun findAllWithCount(param: QueryParam): DataAndCount<JsonMap>

    suspend fun findAllWithCount(param: QueryParam, countKey: FieldExpression): DataAndCount<JsonMap>

    suspend fun query(param: QueryArrayParam): DataGrid

    suspend fun query(param: QueryArrayParam, countKey: FieldExpression): DataGridAndCount

    suspend fun queryObjects(param: QueryArrayParam): List<JsonMap>

    suspend fun queryObjects(param: QueryArrayParam, countKey: FieldExpression): DataAndCount<JsonMap>

    suspend fun <T> querySingle(param: QueryArrayParam): T

    suspend fun upsert(entity: String, data: JsonMap): JsonMap

    suspend fun upsertAll(entity: String, jsonObjects: Stream<JsonMap>): Stream<JsonMap>

    suspend fun <T> delete(entity: String, id: T): T

    suspend fun <T> deleteAll(entity: String, ids: Stream<T>): Stream<T>

    suspend fun <T> deleteEntity(entity: String, obj: JsonMap): JsonMap

    suspend fun <T> deleteAllEntities(entity: String, objects: Stream<JsonMap>): Stream<JsonMap>

    suspend fun execute(param: ExecuteParam): Stream<ExecuteParam>

    suspend fun executeAll(params: Stream<ExecuteParam>): Stream<ExecuteParam>
}