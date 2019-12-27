package tai.orm.upsert

import tai.base.JsonMap

/**
 * Created by Jango on 2017-01-09.
 */
@FunctionalInterface
interface BelongToHandler {
    fun upsert(
        entity: JsonMap,
        dependencyColumnValues: JsonMap,
        upsertContext: UpsertContext
    ): TableData
}