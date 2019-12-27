package tai.orm.upsert.ex

import tai.orm.ex.OrmException

/**
 * Created by Jango on 2017-01-23.
 */
class MissingUpsertFunctionException(message: String) : OrmException(message)