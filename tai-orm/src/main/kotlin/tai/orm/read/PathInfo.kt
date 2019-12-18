package tai.orm.read

import tai.base.ThreadUnsafe
import tai.orm.core.PathExpression
import tai.orm.entity.EntityMappingHelper
import tai.orm.entity.Field
import tai.orm.read.ex.InvalidPrimaryKeyIndexException
import tai.orm.read.ex.ObjectReaderException

@ThreadUnsafe
class PathInfo(
    val helper: EntityMappingHelper,
    val rootEntity: String,
    val fieldAndIndexPairs: MutableSet<FieldAndIndexPair> = mutableSetOf(),
    val directRelations: MutableSet<PathExpression> = mutableSetOf(),
    val indirectRelations: MutableSet<PathExpression> = mutableSetOf()
) {
    private var primaryKeyIndex = -1

    constructor(
        helper: EntityMappingHelper,
        rootEntity: String,
        fieldAndIndexPairs: MutableSet<FieldAndIndexPair>,
        directRelations: MutableSet<PathExpression>,
        indirectRelations: MutableSet<PathExpression>,
        primaryKeyIndex: Int
    ): this(helper, rootEntity, fieldAndIndexPairs, directRelations, indirectRelations) {
        this.primaryKeyIndex = primaryKeyIndex
    }

    fun build(map: Map<PathExpression, PathInfo>): ReadObject {
        if (primaryKeyIndex < 0) {
            throw InvalidPrimaryKeyIndexException("Invalid primary key index $primaryKeyIndex for entity $rootEntity")
        }

        return makeReadObject(
            fieldAndIndexPairs = fieldAndIndexPairs.toList(),
            directRelations = directRelations.map {
                val pathInfo = map[it] ?: throw ObjectReaderException("No pathInfo found for pathExp '$it'")
                DirectRelation(
                    it.last(), makeReadDirectRelation(pathInfo.build(map))
                )
            },
            indirectRelations = indirectRelations.map {
                val pathInfo = map[it] ?: throw ObjectReaderException("No pathInfo found for pathExp '$it'")
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

    override fun toString(): String {
        return "PathInfo(rootEntity='$rootEntity', fieldAndIndexPairs=$fieldAndIndexPairs, directRelations=$directRelations, indirectRelations=$indirectRelations, primaryKeyIndex=$primaryKeyIndex)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PathInfo

        if (rootEntity != other.rootEntity) return false
        if (fieldAndIndexPairs != other.fieldAndIndexPairs) return false
        if (directRelations != other.directRelations) return false
        if (indirectRelations != other.indirectRelations) return false
        if (primaryKeyIndex != other.primaryKeyIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rootEntity.hashCode()
        result = 31 * result + fieldAndIndexPairs.hashCode()
        result = 31 * result + directRelations.hashCode()
        result = 31 * result + indirectRelations.hashCode()
        result = 31 * result + primaryKeyIndex
        return result
    }
}