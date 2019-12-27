package tai.orm.upsert

import tai.base.JsonMap
import tai.orm.ex.InvalidStateException
import java.util.*

data class TableData(
    val table: String,
    val primaryColumns: List<String>,
    val values: JsonMap,
    val isNew: Boolean
) {

    fun withValues(newValues: JsonMap): TableData {
        return copy(
            values = this.values + newValues
        )
    }

    init {
        if (primaryColumns.isEmpty()) {
            throw InvalidStateException("Primary column list is empty in table data for table '$table'")
        }
        primaryColumns.forEach {
            if (values[it] == null) {
                throw InvalidStateException("No value given for primary column '$it' in table data for table '$table'")
            }
        }
    }
}

data class BelongsTo(
    val field: String,
    val belongToHandler: BelongToHandler,
    val dependencyColumnValuePopulator: DependencyColumnValuePopulator
)

data class DirectDependency(
    val field: String,
    val dependencyHandler: DirectDependencyHandler,
    val dependencyColumnValuePopulator: DependencyColumnValuePopulator
)

data class IndirectDependency(val field: String, val indirectDependencyHandler: IndirectDependencyHandler)

data class FieldToColumnMapping(val field: String, val column: String)