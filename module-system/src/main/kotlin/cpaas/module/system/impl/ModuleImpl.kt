package cpaas.module.system.impl

import cpaas.module.system.Module

/**
 * Created by Jango on 9/12/2016.
 */
class ModuleImpl<T>(private val moduleSystem: MutableModuleSystemImpl) : Module<T> {

    override fun <TT> require(tClass: Class<TT>): TT {
        return moduleSystem.require(tClass)
    }

    override fun <TT> require(tClass: Class<TT>, moduleName: String): TT {
        return moduleSystem.require(tClass, moduleName)
    }

    override fun <T1> requireOrElse(tClass: Class<T1>, defaultValue: T1): T1 {
        return moduleSystem.requireOrElse(tClass, defaultValue)
    }

    override fun <T1> requireOrElse(tClass: Class<T1>, moduleName: String, defaultValue: T1): T1 {
        return moduleSystem.requireOrElse(tClass, moduleName, defaultValue)
    }
}