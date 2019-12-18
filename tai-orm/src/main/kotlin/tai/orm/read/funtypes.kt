package tai.orm.read

import tai.base.JsonList
import tai.base.JsonMap

typealias ReadObject = (data: JsonList, dataList: List<JsonList>) -> JsonMap

typealias ReadIndirectRelation = (
    parentId: Any,
    data: JsonList,
    dataList: List<JsonList>
) -> List<JsonMap>

typealias ReadDirectRelation = (data: JsonList, dataList: List<JsonList>) -> JsonMap



