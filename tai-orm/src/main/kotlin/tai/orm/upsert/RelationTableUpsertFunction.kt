package tai.orm.upsert

/**
 * Created by Jango on 2017-01-10.
 */
@FunctionalInterface
interface RelationTableUpsertFunction {
    fun upsert(srcTableData: TableData, dstTableData: TableData, upsertContext: UpsertContext): TableData
}