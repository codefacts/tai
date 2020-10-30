package cpaas.module.system

/**
 * Created by sohan on 5/14/2017.
 */
interface ModuleSystemBuilder {
    fun <T> export(moduleClass: Class<T>, exportScript: ExportScript<T>): ModuleSystemBuilder
    fun <T> export(
        moduleClass: Class<T>,
        moduleName: String,
        exportScript: ExportScript<T>
    ): ModuleSystemBuilder

    fun build(): ModuleSystem
}