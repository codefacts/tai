package tai.orm.update

import tai.orm.delete.DeleteFunction
import tai.orm.upsert.UpsertFunction

data class OperationHolder(
    val upsertFunction: UpsertFunction,
    val deleteFunction: DeleteFunction
)