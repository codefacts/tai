package tai.orm.update.impl

import tai.orm.ex.InvalidStateException
import tai.orm.update.OperationHolder
import tai.orm.update.OperationMap

class OperationMapImpl(private val operationMap: Map<String, OperationHolder>) : OperationMap {
    override fun get(entity: String): OperationHolder {
        return operationMap[entity]
            ?: throw InvalidStateException("No OperationHolder is found for entity '$entity' in the operationMap")
    }
}

class ProxyOperationMapImpl(private var operationMap: Map<String, OperationHolder>) : OperationMap {

    fun setOperationMap(opMap: Map<String, OperationHolder>) {
        this.operationMap = opMap
    }

    override fun get(entity: String): OperationHolder {
        return operationMap[entity]
            ?: throw InvalidStateException("No OperationHolder is found for entity '$entity' in the operationMap")
    }
}