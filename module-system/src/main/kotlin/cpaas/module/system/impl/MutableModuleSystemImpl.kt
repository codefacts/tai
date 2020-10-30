package cpaas.module.system.impl

import cpaas.module.system.ExportScript
import cpaas.module.system.MutableModuleSystem
import cpaas.module.system.ex.ModuleSystemException
import java.util.*

/**
 * Created by Jango on 9/12/2016.
 */
class MutableModuleSystemImpl : MutableModuleSystem {
    private val scriptMap: MutableMap<ModuleSpec, ModuleInfo<*>> = HashMap()
    private val moduleMap: MutableMap<ModuleSpec, Any?> = HashMap()

    override fun <T> require(tClass: Class<T>): T {
        return getOrCreate(tClass, null)
            ?: throw ModuleSystemException("No module is found for type: '$tClass'")
    }

    override fun <T> require(tClass: Class<T>, moduleName: String): T {
        return getOrCreate(tClass, moduleName)
            ?: throw ModuleSystemException("No module is found for type: '$tClass' and name $moduleName")
    }

    override fun <T> requireOrElse(tClass: Class<T>, defaultValue: T): T {
        return getOrCreate(tClass, null) ?: defaultValue
    }

    override fun <T> requireOrElse(tClass: Class<T>, moduleName: String, defaultValue: T): T {
        val module = getOrCreate(tClass, moduleName)
        return module ?: defaultValue
    }

    private fun <T> getOrCreate(moduleClass: Class<T>, moduleName: String?): T? {
        val moduleSpec = ModuleSpec(moduleClass, moduleName)
        var moduleInfo = scriptMap[moduleSpec]
        if (moduleInfo == null) {
            moduleInfo = if (moduleName == null) {
                val spec = scriptMap.keys.stream()
                    .filter { (moduleClass1) -> moduleClass1 == moduleClass }
                    .findFirst()
                    .orElseThrow { ModuleSystemException("Module is not found for type: '$moduleClass'") }
                    ?: return null
                scriptMap[spec]
            } else {
                return null
            }
        }
        if (moduleInfo == null) {
            return null
        }
        return if (moduleInfo.isPrototype) {
            createModule<T>(moduleInfo.exportScript as ExportScript<T>)
        } else {
            findModule(moduleSpec, moduleInfo)
        }
    }

    private fun <T> findModule(moduleSpec: ModuleSpec, moduleInfo: ModuleInfo<*>): T? {
        var module = moduleMap[moduleSpec] as T?
        val moduleClass = moduleSpec.moduleClass
        val moduleName = moduleSpec.moduleName
        if (module == null) {
            module = createModule(moduleInfo.exportScript as ExportScript<T>)
            ensureModuleExportedOrThrow(module, moduleClass as Class<Any>, moduleName)
            moduleMap[moduleSpec] = module

            //Check Default Exists or register this module as default
            moduleMap.putIfAbsent(ModuleSpec(moduleClass, null), module)
        }
        return module
    }

    override fun <T> export(
        moduleClass: Class<T>,
        exportScript: ExportScript<T>
    ): MutableModuleSystemImpl {
        doExport(ModuleSpec(moduleClass), exportScript)
        return this
    }

    override fun <T> export(
        moduleClass: Class<T>,
        moduleName: String,
        exportScript: ExportScript<T>
    ): MutableModuleSystemImpl {
        doExport(ModuleSpec(moduleClass, moduleName), exportScript)
        return this
    }

    fun <T> doExport(
        spec: ModuleSpec,
        exportScript: ExportScript<T>
    ): MutableModuleSystemImpl {
        scriptMap[spec] = ModuleInfo(exportScript, DEFALUT_PROTOTYPE)
        return this
    }

    private fun <T> createModule(exportScript: ExportScript<T>): T {
        return ModuleImpl<T>(this).exportScript()
    }

    private fun <T> ensureModuleExportedOrThrow(
        module: Any?,
        tClass: Class<T>,
        moduleName: String?
    ) {
        if (module == null) {
            throw ModuleSystemException(
                "Module is not exported after script execution for type: '" + tClass + "'" +
                        if (moduleName == null) "" else " and moduleName: '$moduleName'"
            )
        }
    }

    companion object {
        private const val DEFALUT_PROTOTYPE = false

        @JvmStatic
        fun main(args: Array<String>) {
        }
    }
}