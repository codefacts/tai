package tai.orm.upsert.impl

import tai.base.JsonMap
import tai.orm.upsert.DirectDependencyHandler
import tai.orm.upsert.TableData
import tai.orm.upsert.TableDataPopulator
import tai.orm.upsert.UpsertContext
import java.util.*

/**
 * Created by sohan on 7/7/2017.
 */
class TableDataPopulatingDirectDependencyHandlerImpl(val tableDataPopulator: TableDataPopulator) :
    DirectDependencyHandler {

    override fun upsert(
        entity: JsonMap,
        upsertContext: UpsertContext
    ): TableData {
        return tableDataPopulator.populate(entity, false)
    }
}