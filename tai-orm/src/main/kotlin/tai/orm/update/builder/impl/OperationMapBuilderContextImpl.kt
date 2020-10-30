package tai.orm.update.builder.impl

import com.google.common.collect.ImmutableMap
import com.google.common.collect.Maps
import tai.orm.ex.InvalidStateException
import tai.orm.update.OperationHolder
import tai.orm.update.OperationMap
import tai.orm.update.builder.OperationMapBuilderContext
import tai.orm.update.impl.OperationMapImpl
import tai.orm.update.impl.ProxyOperationMapImpl

class OperationMapBuilderContextImpl : OperationMapBuilderContext {
    private val map = Maps.newHashMap<String, OptionalHolder<OperationHolder>>()
    private val proxyOperationMapImpl = ProxyOperationMapImpl(mapOf())

    override fun get(entity: String): OperationHolder {
        return map[entity]?.value
            ?: throw InvalidStateException("No value present for entity '$entity' in the operation map")
    }

    override fun isEmpty(entity: String): Boolean {
        val holder = map[entity]
        return holder != null && holder.value == null
    }

    override fun containsValue(entity: String): Boolean {
        val holder = map[entity]
        return holder?.value != null
    }

    override fun containsEmpty(entity: String): Boolean {
        val holder = map[entity]
        return holder != null && holder.value == null
    }

    override fun put(entity: String, operationHolder: OperationHolder) {
        map[entity] = OptionalHolder(operationHolder)
    }

    override fun putEmpty(entity: String) {
        map[entity] = OptionalHolder<OperationHolder>(null)
    }

    override fun createProxyUpsertFunction(entity: String): ProxyUpsertFunction {
        return ProxyUpsertFunction(
            entityName = entity,
            operationMap = proxyOperationMapImpl
        )
    }

    override fun createProxyDeleteFunction(entity: String): ProxyDeleteFunction {
        return ProxyDeleteFunction(
            entityName = entity,
            operationMap = proxyOperationMapImpl
        )
    }

    override fun build(): OperationMap {

        val operationMap = ImmutableMap.copyOf(
            map.entries.map {
                it.key to (it.value.value ?: throw InvalidStateException("No operationHolder found for entity '${it.key}' in the operation map"))
            }.toMap()
        )

        proxyOperationMapImpl.setOperationMap(operationMap)
        return OperationMapImpl(operationMap)
    }

    class OptionalHolder<T>(val value: T?)
}

