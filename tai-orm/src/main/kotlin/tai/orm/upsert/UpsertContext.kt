package tai.orm.upsert

/**
 * Created by Jango on 2017-01-09.
 */
interface UpsertContext {
    fun put(tableAndPrimaryKey: String, tableData: TableData): UpsertContext
    fun putOrMerge(tableAndPrimaryKey: String, tableData: TableData): UpsertContext
    operator fun get(tableAndPrimaryKey: String): TableData
    fun exists(tableAndPrimaryKey: String): Boolean
}