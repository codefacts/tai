package cpaas.module.system.impl

import cpaas.module.system.ExportScript
import cpaas.module.system.ModuleProvider
import cpaas.module.system.ex.ModuleSystemException
import java.util.*

/**
 * Created by sohan on 5/14/2017.
 */
class ModuleProviderImpl<T>(val exportScript: ExportScript<T>, private val moduleMapProxy: ModuleMapProxy) : ModuleProvider<T> {

    private var exportedModule: T? = null

    override fun get(): T {
        if (exportedModule == null) {
            exportedModule = executeScript()
        }
        return exportedModule!!
    }

    private fun executeScript(): T {
        return ImmutableModuleImpl<T>(moduleMapProxy.map).exportScript()
    }
}