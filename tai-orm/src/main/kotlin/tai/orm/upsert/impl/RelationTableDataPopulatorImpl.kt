package tai.orm.upsert.impl

import tai.base.JsonMap
import tai.base.MutableJsonMap
import tai.orm.ColumnToColumnMapping
import tai.orm.entity.ForeignColumnMapping
import tai.orm.upsert.RelationTableDataPopulator
import tai.orm.upsert.TableData
import java.util.*

/**
 * Created by Jango on 2017-01-10.
 */
class RelationTableDataPopulatorImpl(
    val relationTable: String,
    val srcMappings: List<ForeignColumnMapping>,
    val dstMappings: List<ForeignColumnMapping>
) : RelationTableDataPopulator {

    override fun populate(srcTableData: TableData, dstTableData: TableData, isNew: Boolean): TableData {
        val srcValues = srcTableData.values
        val dstValues = dstTableData.values
        val map: MutableJsonMap = HashMap()

        for ((srcColumn, dstColumn) in srcMappings) {
            map[dstColumn] = srcValues[srcColumn]
        }
        for ((srcColumn, dstColumn) in dstMappings) {
            map[dstColumn] = dstValues[srcColumn]
        }
        return TableData(
            relationTable,
            map.keys.toList(),
            map, isNew
        )
    }

}