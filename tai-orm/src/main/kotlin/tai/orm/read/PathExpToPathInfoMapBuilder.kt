package tai.orm.read

import tai.base.ThreadUnsafe
import tai.orm.core.FieldExpression
import tai.orm.core.PathExpression
import tai.orm.entity.EntityMappingHelper
import tai.orm.entity.Field
import tai.orm.entity.Relationship
import tai.orm.read.ex.ObjectReaderException
import java.util.*

typealias Index = Int

@ThreadUnsafe
internal class PathExpToPathInfoMapBuilder(
    val fieldExpressionToIndexMap: Map<FieldExpression, Index>,
    val rootAlias: String,
    val rootEntity: String,
    val helper: EntityMappingHelper,
    val aliasToFullPathExpressionMap: Map<String, PathExpression>
) {
    fun build(): Map<PathExpression, PathInfo> {

        val map: MutableMap<PathExpression, PathInfo> = HashMap()

        for ((fieldExp, index) in fieldExpressionToIndexMap) {
            val pathExpression: PathExpression = toFullPathExp(
                fieldExp.toPathExpression()
            )
            process(pathExpression, index, map)
        }
        return map
    }

    private fun process(pathExpression: PathExpression, index: Int, map: MutableMap<PathExpression, PathInfo>) {
        val parts: List<String> = pathExpression.parts()
        var entity: String = rootEntity
        var i = 1
        val end = parts.size - 1
        while (i < end) {
            val fieldName = parts[i]
            val field: Field = helper.getField(entity, fieldName)
            val pathInfo: PathInfo = getOrCreatePathInfo(pathExpression.subPath(0, i), map)
            when (field.relationship?.name) {
                Relationship.Name.HAS_ONE -> {
                    pathInfo.directRelations.add(
                        pathExpression.subPath(0, i + 1)
                    )
                }
                Relationship.Name.HAS_MANY -> {
                    pathInfo.indirectRelations.add(
                        pathExpression.subPath(0, i + 1)
                    )
                }
            }
            entity = field.relationship?.entity ?: throw ObjectReaderException("No relationship exists in entity $entity.$field")
            i++
        }
        val field: Field = helper.getField(entity, pathExpression.last())
        val pathInfo: PathInfo = getOrCreatePathInfo(pathExpression.subPath(0, pathExpression.size() - 1), map)
        if (helper.getPrimaryKey(entity) == field.name) {
            pathInfo.setPrimaryKeyIndex(index)
        }
        pathInfo.fieldAndIndexPairs.add(
                FieldAndIndexPair(field.name, index)
            )
    }

    private fun toFullPathExp(pathExpression: PathExpression): PathExpression {
        if (!pathExpression.startsWith(rootAlias)) {
            val fullPath: PathExpression = aliasToFullPathExpressionMap.get(pathExpression.root())
                ?: throw ObjectReaderException("Invalid path expression '$pathExpression' not found in the aliasToFullPathExpressionMap")
            return fullPath.concat(
                pathExpression.subPath(1, pathExpression.size())
            )
        }
        return pathExpression
    }

    private fun getOrCreatePathInfo(pathExpression: PathExpression, map: MutableMap<PathExpression, PathInfo>): PathInfo {
        var pathInfo: PathInfo? = map[pathExpression]
        if (pathInfo == null) {
            map[pathExpression] =
                PathInfo(helper, rootEntity).also { pathInfo = it }
        }
        return pathInfo!!
    }
}