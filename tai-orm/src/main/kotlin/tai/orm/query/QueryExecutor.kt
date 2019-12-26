package tai.orm.query

import tai.base.JsonMap
import tai.orm.*
import tai.orm.core.FieldExpression

interface QueryExecutor {

    suspend fun findAll(param: QueryParam): List<JsonMap>

    suspend fun findAll(param: QueryParam, countKey: FieldExpression): DataAndCount<JsonMap>

    suspend fun queryForDataGrid(param: QueryArrayParam): DataGrid

    suspend fun queryForDataGrid(param: QueryArrayParam, countKey: FieldExpression): DataGridAndCount

    suspend fun queryForObjects(param: QueryArrayParam): List<JsonMap>

    suspend fun queryForObjects(param: QueryArrayParam, countKey: FieldExpression): DataAndCount<JsonMap>
}