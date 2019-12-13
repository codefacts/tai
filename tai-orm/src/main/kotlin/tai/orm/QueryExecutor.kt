package tai.orm

import tai.base.JsonList
import tai.base.JsonMap

interface QueryExecutor {

    suspend fun findAll(param: QueryParam): List<JsonMap>

    suspend fun findAll(param: QueryParam, countKey: String): DataAndCount<JsonMap>

    suspend fun queryForDataGrid(param: QueryArrayParam): DataGrid

    suspend fun queryForDataGrid(param: QueryArrayParam, countKey: String): DataGridAndCount

    suspend fun queryForObjects(param: QueryArrayParam): List<JsonMap>

    suspend fun queryForObjects(param: QueryArrayParam, countKey: String): DataAndCount<JsonMap>
}