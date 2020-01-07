package tai.orm.update.builder.impl

import tai.base.JsonMap
import tai.orm.delete.DeleteContext
import tai.orm.delete.DeleteFunction
import tai.orm.update.OperationMap

class ProxyDeleteFunction(val entityName: String, val operationMap: OperationMap) : DeleteFunction {

    override fun delete(entity: JsonMap, deleteContext: DeleteContext): JsonMap {
        return operationMap.get(entityName).deleteFunction.delete(entity, deleteContext)
    }
}