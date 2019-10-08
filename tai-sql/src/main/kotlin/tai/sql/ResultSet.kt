package tai.sql

import tai.base.JsonList
import tai.base.JsonMap

interface ResultSet {
    val columnNames: List<String>;
    val columnCount: Int;
    val rowCount: Int;
    val output: JsonList?;
    val results: List<JsonList>
    val rows: List<JsonMap>
}
