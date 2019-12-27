package tai.orm.upsert.impl

import tai.orm.upsert.RelationTableDataPopulator
import tai.orm.upsert.RelationTableUpsertFunction
import tai.orm.upsert.TableData
import tai.orm.upsert.UpsertContext
import tai.orm.upsert.UpsertUtils.Companion.toTableAndPrimaryColumnsKey
import java.util.*

/**
 * Created by Jango on 2017-01-10.
 */
class RelationTableUpsertFunctionImpl(val relationTableDataPopulator: RelationTableDataPopulator) :
    RelationTableUpsertFunction {

    override fun upsert(
        srcTableData: TableData,
        dstTableData: TableData,
        upsertContext: UpsertContext
    ): TableData {

        val tableData: TableData = relationTableDataPopulator.populate(srcTableData, dstTableData, true)

        upsertContext.putOrMerge(
            toTableAndPrimaryColumnsKey(
                tableData.table,
                tableData.primaryColumns,
                tableData.values
            ),
            tableData
        )

        return tableData
    }

}