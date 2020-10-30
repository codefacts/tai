package cpaas.module.system

/**
 * Created by Jango on 9/12/2016.
 */
interface Module<TT> {
    fun <T> require(tClass: Class<T>): T
    fun <T> require(tClass: Class<T>, moduleName: String): T
    fun <T> requireOrElse(tClass: Class<T>, defaultValue: T): T
    fun <T> requireOrElse(tClass: Class<T>, moduleName: String, defaultValue: T): T
}