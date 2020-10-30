package tai.orm.upsert.impl

import tai.base.JsonMap
import tai.base.MutableJsonMap
import tai.orm.upsert.FieldToColumnMapping
import tai.orm.upsert.TableData
import tai.orm.upsert.TableDataPopulator
import java.util.*

/**
 * Created by Jango on 2017-01-10.
 */
class TableDataPopulatorImpl(
    val table: String,
    val primaryColumn: String,
    val fieldToColumnMappings: List<FieldToColumnMapping>
) : TableDataPopulator {

    override fun populate(jsonObject: JsonMap, isNew: Boolean): TableData {
        val map: MutableJsonMap = LinkedHashMap()
        for ((field, column) in fieldToColumnMappings) {
            if (jsonObject.containsKey(field)) {
                map[column] = jsonObject[field]
            }
        }
        return TableData(table, listOf(primaryColumn), map, isNew)
    }

}