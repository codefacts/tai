package tai.orm.read

import tai.base.JsonMap
import tai.base.MutableJsonMap
import tai.orm.read.ex.ObjectReaderException
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors

fun makeReadObject(
    fieldAndIndexPairs: List<FieldAndIndexPair>,
    directRelations: List<DirectRelation>,
    indirectRelations: List<IndirectRelation>,
    primaryKeyIndex: Int
): ReadObject {
    return { data, dataList ->
        val mutableMap: MutableJsonMap = mutableMapOf()
        fieldAndIndexPairs.forEach { (field, index) ->
            mutableMap[field] = data[index]
        }
        directRelations.forEach { mutableMap.put(it.field, it.readDirectRelation(data, dataList)) }
        indirectRelations.forEach {
            val parentId = data[primaryKeyIndex] ?: throw ObjectReaderException("Data does not contains ID in Parent.field '${it.field}'")
            mutableMap.put(it.field, it.indirectRelationReader(parentId, data, dataList))
        }
        mutableMap.toMap()
    }
}

fun makeReadDirectRelation(readObject: ReadObject): ReadDirectRelation {
    return readObject
}

fun makeReadIndirectRelation(primaryField: String, readObject: ReadObject, parentPrimaryKeyIndex: Int): ReadIndirectRelation {
    return { parentId, data, dataList ->
        dataList.asSequence().filter { row -> row[parentPrimaryKeyIndex] == parentId }
            .map { row -> readObject(row, dataList) }
            .groupBy(keySelector = {map: JsonMap -> map[primaryField] }, valueTransform = {it})
            .map { entry -> entry.value[0] }
    }
}