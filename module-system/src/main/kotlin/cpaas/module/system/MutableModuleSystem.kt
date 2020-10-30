package cpaas.module.system

import cpaas.module.system.impl.MutableModuleSystemImpl

/**
 * Created by Jango on 9/12/2016.
 */
interface MutableModuleSystem : ModuleSystem {
    fun <T> export(moduleClass: Class<T>, exportScript: ExportScript<T>): MutableModuleSystem
    fun <T> export(
        moduleClass: Class<T>,
        moduleName: String,
        exportScript: ExportScript<T>
    ): MutableModuleSystem

    companion object {
        fun create(): MutableModuleSystem {
            return MutableModuleSystemImpl()
        }
    }
}