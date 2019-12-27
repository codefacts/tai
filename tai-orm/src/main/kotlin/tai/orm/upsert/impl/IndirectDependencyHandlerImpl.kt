package tai.orm.upsert.impl

import tai.base.JsonMap
import tai.orm.upsert.*
import java.util.*

/**
 * Created by Jango on 2017-01-09.
 */
class IndirectDependencyHandlerImpl(
    val relationTableUpserFunction: RelationTableUpsertFunction,
    val childUpsertFunction: UpsertFunction
) : IndirectDependencyHandler {

    override fun upsert(
        parentTableData: TableData,
        entity: JsonMap,
        upsertContext: UpsertContext
    ): TableData {

        val childTableData = childUpsertFunction.upsert(entity, upsertContext)
        relationTableUpserFunction.upsert(parentTableData, childTableData, upsertContext)
        return childTableData
    }
}