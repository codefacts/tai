package tai.sql

import tai.base.JsonList
import tai.base.JsonMap

interface ResultSet {
    val columnCount: Int;
    val rowCount: Int;
    val columnNames: List<String>;
    val results: List<JsonList>
    val output: JsonList?;
    fun toJsonMaps(): List<JsonMap>;
}

interface UpdateResult {
    val updatedCount: Int;
    val columns: List<String>;
    val result: JsonList
}
