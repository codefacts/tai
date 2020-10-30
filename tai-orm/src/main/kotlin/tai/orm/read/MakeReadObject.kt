package tai.orm.read

import tai.base.JsonMap
import tai.base.MutableJsonMap
import tai.orm.core.FieldExpression
import tai.orm.core.PathExpression
import tai.orm.entity.EntityMappingHelper
import tai.orm.read.ex.ObjectReaderException

fun makeConvertToObjects(readObject: ReadObject, primaryKey: String): ConvertToObjects {
    return { dataList ->
        dataList.map { readObject(it, dataList) }.map { obj -> obj[primaryKey] to obj }.toMap().values.toList()
    }
}

fun makeReadObject(
    fieldExpressionToIndexMap: Map<FieldExpression, Index>,
    rootAlias: String,
    rootEntity: String,
    helper: EntityMappingHelper,
    aliasToFullPathExpressionMap: Map<String, PathExpression>
): ReadObject {

    val pathExpToPathInfoMap = PathExpToPathInfoMapBuilder(fieldExpressionToIndexMap, rootAlias, rootEntity, helper, aliasToFullPathExpressionMap).build()

    val pathInfo: PathInfo = pathExpToPathInfoMap.get(PathExpression.create(rootAlias))
        ?: throw ObjectReaderException("No PathInfo found in pathExpToPathInfoMap for key root alias '$rootAlias'")

    return pathInfo.build(pathExpToPathInfoMap)
}

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
        mutableMap
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