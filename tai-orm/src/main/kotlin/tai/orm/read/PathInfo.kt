package tai.orm.read

import tai.orm.core.PathExpression
import tai.orm.entity.EntityMappingHelper
import tai.orm.entity.Field
import tai.orm.read.ex.InvalidPrimaryKeyIndexException
import tai.orm.read.ex.ObjectReaderException

class PathInfo(val helper: EntityMappingHelper, val rootEntity: String) {
    val fieldAndIndexPairs: MutableSet<FieldAndIndexPair> = mutableSetOf()
    val directRelations: MutableSet<PathExpression> = mutableSetOf()
    val indirectRelations: MutableSet<PathExpression> = mutableSetOf()
    private var primaryKeyIndex = -1

    fun build(map: Map<PathExpression, PathInfo>): ReadObject {
        if (primaryKeyIndex < 0) {
            throw InvalidPrimaryKeyIndexException("Invalid primary key index $primaryKeyIndex for entity $rootEntity")
        }

        return makeReadObject(
            fieldAndIndexPairs = fieldAndIndexPairs.toList(),
            directRelations = directRelations.map {
                val pathInfo = map.get(it) ?: throw ObjectReaderException("No pathInfo found for pathExp '$it'")
                DirectRelation(
                    it.last(), makeReadDirectRelation(pathInfo.build(map))
                )
            },
            indirectRelations = indirectRelations.map {
                val pathInfo = map.get(it) ?: throw ObjectReaderException("No pathInfo found for pathExp '$it'")
                IndirectRelation(
                    it.last(), makeReadIndirectRelation(
                        primaryKey(it),
                        pathInfo.build(map),
                        primaryKeyIndex
                    )
                )
            },
            primaryKeyIndex = primaryKeyIndex
        )
    }

    private fun primaryKey(pathExpression: PathExpression): String {
        var entity = rootEntity
        val parts: List<String> = pathExpression.parts()
        for (i in 1 until parts.size) {
            val field: Field = helper.getField(entity, parts[i])
            if (field.relationship == null) {
                throw ObjectReaderException("No child entity found for '" + entity + "." + parts[i] + "'")
            }
            entity = field.relationship.entity
        }
        return helper.getPrimaryKey(entity)
    }

    fun setPrimaryKeyIndex(primaryKeyIndex: Int): PathInfo {
        this.primaryKeyIndex = primaryKeyIndex
        return this
    }
}