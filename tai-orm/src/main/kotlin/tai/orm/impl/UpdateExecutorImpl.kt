package tai.orm.impl

import tai.orm.DeleteParam
import tai.orm.ExecuteParam
import tai.orm.UpdateExecutor
import tai.orm.UpsertParam

class UpdateExecutorImpl : UpdateExecutor {
    override suspend fun upsert(param: UpsertParam): UpsertParam {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(param: DeleteParam) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun execute(params: Collection<ExecuteParam>): Collection<ExecuteParam> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}