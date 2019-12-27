package tai.orm.upsert.impl

import tai.base.JsonMap
import tai.base.MutableJsonMap
import tai.orm.entity.ForeignColumnMapping
import tai.orm.upsert.DependencyColumnValuePopulator
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * Created by Jango on 2017-01-10.
 */
class DependencyColumnValuePopulatorImpl(val foreignColumnMappings: List<ForeignColumnMapping>) :
    DependencyColumnValuePopulator {

    override fun populate(childTableData: JsonMap): JsonMap {

        val mapBuilder: MutableJsonMap = LinkedHashMap()
        for ((srcColumn, dstColumn) in foreignColumnMappings) {
            mapBuilder.put(
                srcColumn,
                childTableData[dstColumn]
            )
        }
        return mapBuilder
    }
}