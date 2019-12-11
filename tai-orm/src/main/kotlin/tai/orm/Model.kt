package tai.orm

import sun.tools.tree.FieldExpression
import tai.base.JsonMap
import tai.criteria.operators.JoinType
import tai.orm.core.PathExpression
import java.util.*

data class CountDistinctParam(
    val countingKey: FieldExpression,
    val entity: String,
    val alias: String,
    val joinParams: Collection<JoinParam>,
    val criteria: JsonMap? = null,
    val groupBy: JsonMap,
    val having: JsonMap? = null
)

data class JoinParam(
    val path: PathExpression,
    val alias: String,
    val joinType: JoinType = JoinType.JOIN
)

data class DataAndCount<T>(val data: List<T>, val count: Long)

open class QueryParamBase(
    open val entity: String,
    open val alias: String,
    open val joinParams: Collection<JoinParam>,
    open val criteria: JsonMap? = null,
    open val orderBy: JsonMap? = null,
    open val groupBy: JsonMap? = null,
    open val having: JsonMap? = null,
    open val pagination: Pagination? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QueryParamBase

        if (entity != other.entity) return false
        if (alias != other.alias) return false
        if (joinParams != other.joinParams) return false
        if (criteria != other.criteria) return false
        if (orderBy != other.orderBy) return false
        if (groupBy != other.groupBy) return false
        if (having != other.having) return false
        if (pagination != other.pagination) return false

        return true
    }

    override fun hashCode(): Int {
        var result = entity.hashCode()
        result = 31 * result + alias.hashCode()
        result = 31 * result + joinParams.hashCode()
        result = 31 * result + (criteria?.hashCode() ?: 0)
        result = 31 * result + (orderBy?.hashCode() ?: 0)
        result = 31 * result + (groupBy?.hashCode() ?: 0)
        result = 31 * result + (having?.hashCode() ?: 0)
        result = 31 * result + (pagination?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "QueryParamBase(entity='$entity', alias='$alias', joinParams=$joinParams, criteria=$criteria, orderBy=$orderBy, groupBy=$groupBy, having=$having, pagination=$pagination)"
    }

}

data class QueryParam(
    override val entity: String,
    override val alias: String,
    override val joinParams: Collection<JoinParam>,
    val selections: Collection<FieldExpression>,
    override val criteria: JsonMap? = null,
    override val orderBy: JsonMap? = null,
    override val groupBy: JsonMap? = null,
    override val having: JsonMap? = null,
    override val pagination: Pagination? = null
): QueryParamBase(entity, alias, joinParams, criteria, orderBy, groupBy, having, pagination)

data class Pagination(
    val fieldExpression: FieldExpression,
    val offset: Long = 0,
    val size: Int
)

data class QueryArrayParam(
    override val entity: String,
    override val alias: String,
    override val joinParams: Collection<JoinParam>,
    val selections: Collection<JsonMap>,
    override val criteria: JsonMap? = null,
    override val orderBy: JsonMap? = null,
    override val groupBy: JsonMap? = null,
    override val having: JsonMap? = null,
    override val pagination: Pagination? = null
): QueryParamBase(entity, alias, joinParams, criteria, orderBy, groupBy, having)

data class UpsertParam(
    val entity: String,
    val jsonObject: JsonMap
)

data class DeleteParam(
    val entity: String,
    val jsonObject: JsonMap
)

data class ExecuteParam(
    val operationType: OperationType,
    val entity: String,
    val jsonObject: JsonMap
)

enum class OperationType {
    UPSERT, DELETE
}