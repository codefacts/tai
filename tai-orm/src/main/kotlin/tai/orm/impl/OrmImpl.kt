package tai.orm.impl

import tai.base.JsonMap
import tai.orm.*
import tai.orm.core.FieldExpression

class OrmImpl : Orm {
    override suspend fun countDistinct(entity: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun countDistinct(param: CountDistinctParam): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun <T> findOne(param: QueryParam): JsonMap {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun findAll(param: QueryParam): List<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun findAll(param: QueryParam, countKey: FieldExpression): DataAndCount<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun queryForDataGrid(param: QueryArrayParam): DataGrid {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun queryForDataGrid(param: QueryArrayParam, countKey: FieldExpression): DataGridAndCount {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun queryForObjects(param: QueryArrayParam): List<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun queryForObjects(param: QueryArrayParam, countKey: FieldExpression): DataAndCount<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun <T> querySingle(param: QueryArrayParam): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun upsert(entity: String, data: JsonMap): JsonMap {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun upsertAll(entity: String, jsonObjects: List<JsonMap>): List<JsonMap> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun <T> delete(entity: String, id: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun <T> deleteAll(entity: String, ids: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun <T> deleteEntity(entity: String, obj: JsonMap) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun <T> deleteAllEntities(entity: String, objects: List<JsonMap>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun execute(param: ExecuteParam): ExecuteParam {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun executeAll(params: Collection<ExecuteParam>): Collection<ExecuteParam> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}