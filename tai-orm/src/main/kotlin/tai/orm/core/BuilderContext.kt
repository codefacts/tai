package tai.orm.core

/**
 * Created by sohan on 3/28/2017.
 */
interface BuilderContext<T> {
    fun putEmpty(key: String): BuilderContext<T>
    fun isEmpty(key: String): Boolean
    operator fun contains(key: String): Boolean
    operator fun get(key: String): T
    fun put(entity: String, upsertEventDispatcher: T): BuilderContext<T>
    fun makeImmutable(): BuilderContext<T>
}