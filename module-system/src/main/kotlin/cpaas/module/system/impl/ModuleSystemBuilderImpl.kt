package cpaas.module.system.impl

import com.google.common.collect.ImmutableMap
import tai.base.BaseUtils.not
import cpaas.module.system.ExportScript
import cpaas.module.system.ModuleProvider
import cpaas.module.system.ModuleSystem
import cpaas.module.system.ModuleSystemBuilder
import cpaas.module.system.impl.ModuleSystemImpl.TypeAndNamePair
import java.util.*

/**
 * Created by sohan on 5/14/2017.
 */
class ModuleSystemBuilderImpl : ModuleSystemBuilder {
    val typeAndNamePairToModuleHolderMap: MutableMap<TypeAndNamePair, ModuleProvider<Any>> = LinkedHashMap()
    val moduleMapProxy = ModuleMapProxy()

    override fun <T> export(
        moduleClass: Class<T>,
        exportScript: ExportScript<T>
    ): ModuleSystemBuilderImpl {
        put(
            TypeAndNamePair(moduleClass, null),
            exportScript
        )
        return this
    }

    override fun <T> export(
        moduleClass: Class<T>,
        moduleName: String,
        exportScript: ExportScript<T>
    ): ModuleSystemBuilderImpl {
        if (not(
                typeAndNamePairToModuleHolderMap.containsKey(
                    TypeAndNamePair(moduleClass, moduleName)
                )
            )
        ) {
            put(
                TypeAndNamePair(moduleClass, null),
                exportScript
            )
        }
        put(
            TypeAndNamePair(moduleClass, moduleName),
            exportScript
        )
        return this
    }

    override fun build(): ModuleSystem {
        val map = ImmutableMap.copyOf(typeAndNamePairToModuleHolderMap)
        return createImmutableModuleSystem(map)
    }

    private fun createImmutableModuleSystem(map: ImmutableMap<TypeAndNamePair, ModuleProvider<Any>>): ModuleSystem {
        moduleMapProxy.setMap(map)
        return ModuleSystemImpl(map)
    }

    private fun <T> put(typeAndNamePair: TypeAndNamePair, exportScript: ExportScript<T>) {
        typeAndNamePairToModuleHolderMap[typeAndNamePair] = ModuleProviderImpl(exportScript as ExportScript<Any>, moduleMapProxy)
    }
}