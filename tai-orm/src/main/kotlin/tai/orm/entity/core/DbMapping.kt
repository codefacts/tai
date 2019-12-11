package tai.orm.entity.core

import tai.orm.entity.core.columnmapping.ColumnMapping
import tai.orm.entity.core.columnmapping.RelationMapping
import java.util.*

/**
 * Created by Jango on 2017-01-08.
 */
class DbMapping(
    table: String,
    primaryColumn: String,
    columnMappings: Array<ColumnMapping>,
    relationMappings: Array<RelationMapping>
) {
    val table: String
    val primaryColumn: String
    val columnMappings: Array<ColumnMapping>
    val relationMappings: Array<RelationMapping>

    init {
        Objects.requireNonNull(table)
        Objects.requireNonNull(primaryColumn)
        Objects.requireNonNull(columnMappings)
        Objects.requireNonNull(relationMappings)
        this.table = table
        this.primaryColumn = primaryColumn
        this.columnMappings = columnMappings
        this.relationMappings = relationMappings
    }
}