package tai.orm.core.impl

import tai.orm.Utils.Companion.not
import tai.orm.core.BuilderContext
import tai.orm.core.ex.BuilderContextException
import java.util.*

/**
 * Created by sohan on 3/28/2017.
 */
class BuilderContextImpl<T>(val map: MutableMap<String, Optional<T>>) :
    BuilderContext<T> {

    override fun putEmpty(key: String): BuilderContext<T> {
        map[key] = Optional.empty()
        return this
    }

    override fun isEmpty(key: String): Boolean {
        return map[key]?.isEmpty ?: false
    }

    override fun contains(key: String): Boolean {
        return map[key]?.isPresent ?: false
    }

    override fun get(key: String): T {
        val optional = map[key] ?: throw BuilderContextException("No object found for key '$key'")
        return optional.get()
    }

    override fun put(entity: String, upsertEventDispatcher: T): BuilderContext<T> {
        map[entity] = Optional.of(upsertEventDispatcher)
        return this
    }

    override fun makeImmutable(): BuilderContext<T> {
        return this
    }
}