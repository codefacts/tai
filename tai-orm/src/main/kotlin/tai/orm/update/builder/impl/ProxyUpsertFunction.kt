package tai.orm.update.builder.impl

import tai.base.JsonMap
import tai.orm.update.OperationMap
import tai.orm.upsert.TableData
import tai.orm.upsert.UpsertContext
import tai.orm.upsert.UpsertFunction

class ProxyUpsertFunction(val entityName: String, val operationMap: OperationMap) : UpsertFunction {
    override fun upsert(jsonObject: JsonMap, upsertContext: UpsertContext): TableData {
        return operationMap.get(entityName).upsertFunction.upsert(jsonObject, upsertContext)
    }
}