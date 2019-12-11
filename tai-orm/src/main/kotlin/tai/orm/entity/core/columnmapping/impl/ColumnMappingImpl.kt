package tai.orm.entity.core.columnmapping.impl

import tai.orm.entity.core.DbType
import tai.orm.entity.core.columnmapping.ColumnMapping

/**
 * Created by Jango on 2017-01-12.
 */
class ColumnMappingImpl(override val field: String, override val column: String, override val dbType: DbType) :
    ColumnMapping