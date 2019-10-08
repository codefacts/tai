package tai.sql.ex

import java.lang.RuntimeException

class TaiSqlException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : RuntimeException(message, cause);