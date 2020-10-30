package cpaas.module.system.impl

import cpaas.module.system.ModuleProvider
import cpaas.module.system.ex.ModuleMapProxyException
import cpaas.module.system.impl.ModuleSystemImpl.TypeAndNamePair
import java.util.*

/**
 * Created by sohan on 5/14/2017.
 */
class ModuleMapProxy {
    private var typeAndNamePairToModuleHolderMap: Map<TypeAndNamePair, ModuleProvider<Any>>? = null

    fun setMap(typeAndNamePairToModuleHolderMap: Map<TypeAndNamePair, ModuleProvider<Any>>): ModuleMapProxy {
        this.typeAndNamePairToModuleHolderMap = typeAndNamePairToModuleHolderMap
        return this
    }

    val map: Map<TypeAndNamePair, ModuleProvider<Any>>
        get() {
            if (typeAndNamePairToModuleHolderMap == null) {
                throw ModuleMapProxyException("No ModuleMap is set yet for ModuleMapProxy")
            }
            return typeAndNamePairToModuleHolderMap!!
        }
}