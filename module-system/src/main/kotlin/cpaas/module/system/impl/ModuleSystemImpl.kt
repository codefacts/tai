package cpaas.module.system.impl

import cpaas.module.system.ModuleProvider
import cpaas.module.system.ModuleSystem
import cpaas.module.system.ex.ModuleSystemException
import java.util.*

/**
 * Created by sohan on 5/14/2017.
 */
class ModuleSystemImpl(val typeAndNamePairToModuleHolderMap: Map<TypeAndNamePair, ModuleProvider<Any>>) :
    ModuleSystem {

    override fun <T> require(type: Class<T>): T {
        Objects.requireNonNull(type)
        return this.get<T>(
                TypeAndNamePair(
                    type = type
                )
            ) ?: throw ModuleSystemException("No module found for type '$type'")
    }

    override fun <T> require(type: Class<T>, moduleName: String): T {
        return this.get<T>(TypeAndNamePair(type = type, name = moduleName))
            ?: throw ModuleSystemException("No module found for type '$type' and name '$moduleName'")
    }

    override fun <T> requireOrElse(type: Class<T>, defaultValue: T): T {
        return this.get<T>(TypeAndNamePair(type = type)) ?: defaultValue
    }

    override fun <T> requireOrElse(type: Class<T>, moduleName: String, defaultValue: T): T {
        return this.get<T>(
            TypeAndNamePair(type = type, name = moduleName)
        ) ?: defaultValue
    }

    private operator fun <T> get(typeAndNamePair: TypeAndNamePair): T? {

        val moduleProvider = (typeAndNamePairToModuleHolderMap[typeAndNamePair] ?: return null) as ModuleProvider<T>

        return moduleProvider.get()
            ?: throw ModuleSystemException("No module was exported during script execution for module $typeAndNamePair")
    }

    data class TypeAndNamePair(val type: Class<*>, val name: String? = null)

}