package tai.orm.upsert.builder

import tai.orm.core.BuilderContext
import tai.orm.upsert.UpsertFunction

/**
 * Created by Jango on 2017-01-21.
 */
@FunctionalInterface
interface UpsertFunctionBuilder {
    fun build(entity: String, functionMap: BuilderContext<UpsertFunction>): UpsertFunction
}