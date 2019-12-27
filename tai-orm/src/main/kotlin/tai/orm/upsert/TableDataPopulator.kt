package tai.orm.upsert

import tai.base.JsonMap

/**
 * Created by Jango on 2017-01-09.
 */
@FunctionalInterface
interface TableDataPopulator {
    fun populate(jsonObject: JsonMap, isNew: Boolean): TableData
}