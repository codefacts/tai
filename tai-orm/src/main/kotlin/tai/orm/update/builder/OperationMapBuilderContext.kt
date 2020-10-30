package tai.orm.update.builder

import tai.orm.update.OperationHolder
import tai.orm.update.OperationMap
import tai.orm.update.builder.impl.ProxyDeleteFunction
import tai.orm.update.builder.impl.ProxyUpsertFunction

interface OperationMapBuilderContext {
    fun get(entity: String): OperationHolder
    fun isEmpty(entity: String): Boolean
    fun containsValue(entity: String): Boolean
    fun containsEmpty(entity: String): Boolean
    fun put(entity: String, operationHolder: OperationHolder)
    fun putEmpty(entity: String)

    fun createProxyUpsertFunction(entity: String): ProxyUpsertFunction
    fun createProxyDeleteFunction(entity: String): ProxyDeleteFunction

    fun build(): OperationMap
}