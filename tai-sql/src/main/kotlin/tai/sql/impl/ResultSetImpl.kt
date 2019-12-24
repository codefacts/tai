package tai.sql.impl

import tai.base.JsonList
import tai.base.JsonMap
import tai.base.assertThat
import tai.sql.ResultSet
import tai.sql.ex.TaiSqlException

data class ResultSetImpl(
    override val columnNames: List<String>,
    override val results: List<JsonList>,
    override val output: JsonList? = null
) : ResultSet {
    override val columnCount: Int = columnNames.size
    override val rowCount: Int = results.size

    override fun toJsonMaps(): List<JsonMap> {
        return jsonObjects()
    }

    private fun jsonObjects(): List<JsonMap> {
        return results.map { ja -> jaToJsonObject(ja) };
    }

    private fun jaToJsonObject(jsonArray: JsonList): JsonMap {
        return columnNames.mapIndexed { index, column -> column to jsonArray[index] }.toMap();
    }
}