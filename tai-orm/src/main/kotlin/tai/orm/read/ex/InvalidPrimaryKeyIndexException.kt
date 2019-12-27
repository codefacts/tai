package tai.orm.read.ex

import tai.orm.ex.OrmException

/**
 * Created by sohan on 2017-07-25.
 */
class InvalidPrimaryKeyIndexException(message: String) : OrmException(message)