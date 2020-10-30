package cpaas.module.system

import cpaas.module.system.impl.ModuleSystemBuilderImpl

/**
 * Created by sohan on 5/14/2017.
 */
interface ModuleSystem {
    fun <T> require(tClass: Class<T>): T
    fun <T> require(tClass: Class<T>, moduleName: String): T
    fun <T> requireOrElse(tClass: Class<T>, defaultValue: T): T
    fun <T> requireOrElse(tClass: Class<T>, moduleName: String, defaultValue: T): T

    companion object {
        fun builder(): ModuleSystemBuilder {
            return ModuleSystemBuilderImpl()
        }
    }
}