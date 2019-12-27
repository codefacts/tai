package tai.orm.upsert

import tai.base.JsonMap

/**
 * Created by Jango on 2017-01-09.
 */
@FunctionalInterface
interface IndirectDependencyHandler {
    fun upsert(
        parentTableData: TableData,
        entity: JsonMap,
        upsertContext: UpsertContext
    ): TableData
}