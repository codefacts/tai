package tai.orm.upsert

/**
 * Created by Jango on 2017-01-10.
 */
@FunctionalInterface
interface RelationTableDataPopulator {
    fun populate(srcTableData: TableData, dstTableData: TableData, isNew: Boolean): TableData
}