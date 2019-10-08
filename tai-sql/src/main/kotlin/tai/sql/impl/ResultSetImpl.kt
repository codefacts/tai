package tai.sql.impl

import tai.base.JsonList
import tai.base.JsonMap
import tai.base.assertThat
import tai.sql.ResultSet
import tai.sql.ex.TaiSqlException

class ResultSetImpl(
    override val columnNames: List<String>,
    private val resultAsJsonArrays: List<JsonList>? = null,
    private val resultAsJsonObjects: List<JsonMap>? = null,
    private val outputAsJsonArray: JsonList?
) : ResultSet {
    init {
        assertThat(resultAsJsonArrays != null || resultAsJsonObjects != null) { "resultAsJsonArrays and resultAsJsonObjects can not be null at the same time" };
    }

    private val rowCount0 = resultAsJsonObjects?.size ?: resultAsJsonArrays?.size
    ?: throw TaiSqlException("resultAsJsonArrays and resultAsJsonObjects can not be null at the same time");

    override val columnCount: Int
        get() = columnNames.size
    override val rowCount: Int
        get() = rowCount0
    override val output: JsonList?
        get() = outputAsJsonArray;
    override val results: List<JsonList>
        get() = jsonArrays();
    override val rows: List<JsonMap>
        get() = jsonObjects();

    private fun jsonArrays(): List<JsonList> {
        if (resultAsJsonArrays != null) {
            return resultAsJsonArrays
        }
        return resultAsJsonObjects!!.map { jsonObject -> joToJsonArray(jsonObject); }.toList();
    }

    private fun joToJsonArray(jsonObject: JsonMap): JsonList {
        return columnNames.map { name -> jsonObject[name] }.toList();
    }

    private fun jsonObjects(): List<JsonMap> {
        if (resultAsJsonObjects != null) {
            return resultAsJsonObjects;
        }
        return resultAsJsonArrays!!.map { ja -> jaToJsonObject(ja) }.toList();
    }

    private fun jaToJsonObject(jsonArray: JsonList): JsonMap {
        return columnNames.mapIndexed { index, column -> column to jsonArray[index] }.toMap();
    }
}