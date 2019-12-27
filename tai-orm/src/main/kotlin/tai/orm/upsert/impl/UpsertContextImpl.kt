package tai.orm.upsert.impl

import tai.orm.ex.InvalidStateException
import tai.orm.upsert.TableData
import tai.orm.upsert.UpsertContext
import java.util.*

/**
 * Created by Jango on 2017-01-21.
 */
class UpsertContextImpl(val map: MutableMap<String, TableData> = LinkedHashMap()) :
    UpsertContext {

    override fun put(tableAndPrimaryKey: String, tableData: TableData): UpsertContext {
        map[tableAndPrimaryKey] = tableData
        return this
    }

    override fun putOrMerge(tableAndPrimaryKey: String, tableData: TableData): UpsertContext {
        if (map.containsKey(tableAndPrimaryKey)) {
            val data = map[tableAndPrimaryKey] ?: throw InvalidStateException("Table data does not exists for key '$tableAndPrimaryKey'")
            map[tableAndPrimaryKey] = data.withValues(
                tableData.values
            )
            return this
        }
        map[tableAndPrimaryKey] = tableData
        return this
    }

    override fun get(tableAndPrimaryKey: String): TableData {
        return map[tableAndPrimaryKey]
            ?: throw InvalidStateException("No table data found for key '$tableAndPrimaryKey'")
    }

    override fun exists(tableAndPrimaryKey: String): Boolean {
        return map[tableAndPrimaryKey] != null
    }
}