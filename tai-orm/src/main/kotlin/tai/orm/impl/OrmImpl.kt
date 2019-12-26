package tai.orm.impl

import tai.base.JsonMap
import tai.criteria.ops.count
import tai.criteria.ops.distinct
import tai.orm.*
import tai.orm.core.FieldExpression
import tai.orm.entity.EntityMappingHelper
import tai.orm.query.QueryExecutor
import tai.orm.query.ex.MultipleResultException
import tai.orm.query.ex.NoResultException

class OrmImpl(val executor: QueryExecutor, val helper: EntityMappingHelper) : Orm {

    override suspend fun countDistinct(entity: String): Long {
        return executor.querySingle(
            QueryArrayParam(
                selections = listOf(
                    count(field(FieldExpression.create("r", helper.getPrimaryKey(entity))))
                ),
                entity = entity,
                alias = "r"
            )
        )
    }

    override suspend fun countDistinct(param: CountDistinctParam): Long {
        return executor.querySingle(
            QueryArrayParam(
                selections = listOf(
                    count(distinct(field(param.countingKey)))
                ),
                entity = param.entity,
                alias = param.alias,
                joinParams = param.joinParams,
                criteria = param.criteria,
                groupBy = param.groupBy,
                having = param.having
            )
        )
    }

    override suspend fun <T> findOne(param: QueryParam): JsonMap {
        val data = executor.findAll(param)
        if (data.isEmpty()) {
            throw NoResultException("No result found in query single, expected exactly one result")
        }
        if (data.size > 1) {
            throw MultipleResultException("Multiple result found in query single, expected exactly one result")
        }
        return data[0]
    }

    override suspend fun findAll(param: QueryParam): List<JsonMap> {
        return executor.findAll(param)
    }

    override suspend fun findAllWithCount(param: QueryParam): DataAndCount<JsonMap> {
        return executor.findAllWithCount(param)
    }

    override suspend fun findAllWithCount(param: QueryParam, countKey: FieldExpression): DataAndCount<JsonMap> {
        return executor.findAllWithCount(param, countKey)
    }

    override suspend fun query(param: QueryArrayParam): DataGrid {
        return executor.query(param)
    }

    override suspend fun query(param: QueryArrayParam, countKey: FieldExpression): DataGridAndCount {
        return executor.query(param, countKey)
    }

    override suspend fun queryObjects(param: QueryArrayParam): List<JsonMap> {
        return executor.queryObjects(param)
    }

    override suspend fun queryObjects(param: QueryArrayParam, countKey: FieldExpression): DataAndCount<JsonMap> {
        return executor.queryObjects(param, countKey)
    }

    override suspend fun <T> querySingle(param: QueryArrayParam): T {
        return executor.querySingle(param)
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