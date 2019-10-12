package tai.sql.impl

import tai.base.JsonList
import tai.sql.UpdateResult

data class UpdateResultImpl(
    override val updatedCount: Int,
    override val columns: List<String>,
    override val result: JsonList
) : UpdateResult;