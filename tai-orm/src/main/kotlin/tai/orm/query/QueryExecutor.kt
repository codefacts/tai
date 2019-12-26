package tai.orm.query

import tai.base.JsonMap
import tai.orm.*
import tai.orm.core.FieldExpression

interface QueryExecutor {

    suspend fun findAll(param: QueryParam): List<JsonMap>

    suspend fun findAllWithCount(param: QueryParam): DataAndCount<JsonMap>

    suspend fun findAllWithCount(param: QueryParam, countKey: FieldExpression): DataAndCount<JsonMap>

    suspend fun query(param: QueryArrayParam): DataGrid

    suspend fun query(param: QueryArrayParam, countKey: FieldExpression): DataGridAndCount

    suspend fun <T> querySingle(param: QueryArrayParam): T

    suspend fun queryObjects(param: QueryArrayParam): List<JsonMap>

    suspend fun queryObjects(param: QueryArrayParam, countKey: FieldExpression): DataAndCount<JsonMap>
}