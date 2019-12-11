package tai.orm

interface UpdateExecutor {

    suspend fun upsert(param: UpsertParam): UpsertParam

    suspend fun delete(param: DeleteParam)

    suspend fun execute(params: Collection<ExecuteParam>): Collection<ExecuteParam>
}