package tai.orm.upsert.impl

import tai.base.JsonMap
import tai.orm.upsert.DirectDependencyHandler
import tai.orm.upsert.TableData
import tai.orm.upsert.UpsertContext
import tai.orm.upsert.UpsertFunction
import java.util.*

/**
 * Created by Jango on 2017-01-09.
 */
class DirectDependencyHandlerImpl(val upsertFunction: UpsertFunction) : DirectDependencyHandler {

    override fun requireUpsert(
        entity: JsonMap,
        upsertContext: UpsertContext
    ): TableData {
        return upsertFunction.upsert(entity, upsertContext)
    }
}