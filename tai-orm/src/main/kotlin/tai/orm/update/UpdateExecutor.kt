package tai.orm.update

import tai.orm.DeleteParam
import tai.orm.ExecuteParam
import tai.orm.UpsertParam
import java.util.stream.Stream

interface UpdateExecutor {

    suspend fun upsert(param: UpsertParam): UpsertParam

    suspend fun delete(param: DeleteParam): DeleteParam

    suspend fun executeAll(params: Stream<ExecuteParam>): Stream<ExecuteParam>
}