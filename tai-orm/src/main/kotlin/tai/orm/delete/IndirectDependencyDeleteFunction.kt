package tai.orm.delete

import tai.base.JsonMap
import tai.orm.upsert.TableData

interface IndirectDependencyDeleteFunction {
    fun delete(parentTableData: TableData, childEntity: JsonMap, deleteContext: DeleteContext)
}