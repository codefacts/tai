package cpaas.module.system.impl

import cpaas.module.system.ExportScript

/**
 * Created by Jango on 9/12/2016.
 */
data class ModuleInfo<T>(val exportScript: ExportScript<T>, val isPrototype: Boolean)