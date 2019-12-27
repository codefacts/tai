package tai.orm.upsert.impl

import tai.base.JsonMap
import tai.orm.upsert.BelongToHandler
import tai.orm.upsert.TableData
import tai.orm.upsert.UpsertContext
import tai.orm.upsert.UpsertFunction
import tai.orm.upsert.UpsertUtils.Companion.toTableAndPrimaryColumnsKey
import java.util.*

/**
 * Created by Jango on 2017-01-09.
 */
class BelongToHandlerImpl(val upsertFunction: UpsertFunction) : BelongToHandler {

    override fun upsert(
        entity: JsonMap,
        dependencyColumnValues: JsonMap,
        upsertContext: UpsertContext
    ): TableData {

        var tableData = upsertFunction.upsert(entity, upsertContext)
        tableData = tableData.withValues(
            dependencyColumnValues
        )
        upsertContext.putOrMerge(
            toTableAndPrimaryColumnsKey(
                tableData.table, tableData.primaryColumns, tableData.values
            ),
            tableData
        )
        return tableData
    }
}