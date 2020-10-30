package cpaas.module.system.impl

import cpaas.module.system.Module
import cpaas.module.system.ModuleProvider
import cpaas.module.system.ModuleSystem
import cpaas.module.system.impl.ModuleSystemImpl.TypeAndNamePair
import java.util.*

/**
 * Created by sohan on 5/14/2017.
 */
class ImmutableModuleImpl<TT>(typeAndNamePairToModuleHolderMap: Map<TypeAndNamePair, ModuleProvider<Any>>) :
    Module<TT> {
    val moduleSystem: ModuleSystem

    override fun <T> require(tClass: Class<T>): T {
        return moduleSystem.require(tClass)
    }

    override fun <T> require(tClass: Class<T>, moduleName: String): T {
        return moduleSystem.require(tClass, moduleName)
    }

    override fun <T> requireOrElse(tClass: Class<T>, defaultValue: T): T {
        return moduleSystem.requireOrElse(tClass, defaultValue)
    }

    override fun <T> requireOrElse(tClass: Class<T>, moduleName: String, defaultValue: T): T {
        return moduleSystem.requireOrElse(tClass, moduleName, defaultValue)
    }

    init {
        moduleSystem = ModuleSystemImpl(
            typeAndNamePairToModuleHolderMap
        )
    }
}