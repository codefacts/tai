package tai.orm.entity.core.columnmapping

import tai.orm.entity.core.DbType

/**
 * Created by Jango on 2017-01-12.
 */
interface ColumnMapping {
    val field: String
    val column: String
    val dbType: DbType
}