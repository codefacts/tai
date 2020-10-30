/*
 * (C) Copyright 2006-2020 hSenid Mobile Solutions (Pvt) Limited.
 *
 * All Rights Reserved.
 *
 * These materials are unpublished, proprietary, confidential source code of
 * hSenid Mobile Solutions (Pvt) Limited and constitute a TRADE SECRET
 * of hSenid Mobile Solutions (Pvt) Limited.
 *
 * hSenid Mobile Solutions (Pvt) Limited retains all title to and intellectual
 * property rights in these materials.
 *
 */

package tai.base

import java.util.concurrent.Callable

object BaseUtils {
    @JvmStatic
    fun not(`val`: Boolean): Boolean {
        return !`val`
    }

    @JvmStatic
    fun <T> or(`val`: T?, defaultValue: T): T {
        return `val` ?: defaultValue
    }

    @JvmStatic
    fun isEmptyNullSpace(`val`: String): Boolean {
        return `val`.isEmpty() || `val`.trim { it <= ' ' }.isEmpty()
    }

    @JvmStatic
    fun <T> call(callable: Callable<T>): T {
        return try {
            callable.call()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    fun <T> cast(t: Any): T {
        return t as T
    }
}