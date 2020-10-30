package cpaas.module.system

import java.util.*

/**
 * Created by sohan on 5/14/2017.
 */
@FunctionalInterface
interface ModuleProvider<T> {
    fun get(): T?
}