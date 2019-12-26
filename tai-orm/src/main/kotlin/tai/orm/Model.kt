package tai.orm

import tai.base.JsonList
import tai.base.JsonMap
import tai.criteria.operators.JoinType
import tai.criteria.operators.Order
import tai.orm.core.FieldExpression
import tai.orm.core.PathExpression
import tai.sql.OrderBySpec
import java.util.*

data class CountDistinctParam(
    val countingKey: FieldExpression,
    val entity: String,
    val alias: String,
    val joinParams: Collection<JoinParam> = emptyList(),
    val criteria: List<JsonMap> = emptyList(),
    val groupBy: List<JsonMap> = emptyList(),
    val having: List<JsonMap> = emptyList()
)

data class JoinParam(
    val path: PathExpression,
    val alias: String,
    val joinType: JoinType? = null
)

data class DataAndCount<T>(val data: List<T>, val count: Long)

interface DataGridBase {
    val columns: List<String>
    val data: List<JsonList>
}

data class DataGrid(override val columns: List<String>, override val data: List<JsonList>): DataGridBase

data class DataGridAndCount(
    override val columns: List<String>,
    override val data: List<JsonList>,
    val count: Long
): DataGridBase

data class OrderByData(
    val fieldExpression: JsonMap,
    val order: Order
) {
    constructor(fieldExpression: FieldExpression) : this(field(fieldExpression), Order.ASC)
    constructor(fieldExpression: FieldExpression, order: Order) : this(field(fieldExpression), order)
}

open class QueryParamBase(
    open val entity: String,
    open val alias: String,
    open val joinParams: Collection<JoinParam>,
    open val criteria: List<JsonMap>,
    open val orderBy: List<OrderByData>,
    open val groupBy: List<JsonMap>,
    open val having: List<JsonMap>,
    open val pagination: Pagination? = null
) {

    override fun toString(): String {
        return "QueryParamBase(entity='$entity', alias='$alias', joinParams=$joinParams, criteria=$criteria, orderBy=$orderBy, groupBy=$groupBy, having=$having, pagination=$pagination)"
    }

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
        result = 31 * result + criteria.hashCode()
        result = 31 * result + orderBy.hashCode()
        result = 31 * result + groupBy.hashCode()
        result = 31 * result + having.hashCode()
        result = 31 * result + (pagination?.hashCode() ?: 0)
        return result
    }

}

data class QueryParam(
    override val entity: String,
    override val alias: String,
    override val joinParams: Collection<JoinParam> = emptyList(),
    val selections: Collection<FieldExpression>,
    override val criteria: List<JsonMap> = emptyList(),
    override val groupBy: List<JsonMap> = emptyList(),
    override val having: List<JsonMap> = emptyList(),
    override val orderBy: List<OrderByData> = emptyList(),
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
    override val joinParams: Collection<JoinParam> = emptyList(),
    val selections: Collection<JsonMap>,
    override val criteria: List<JsonMap> = emptyList(),
    override val orderBy: List<OrderByData> = emptyList(),
    override val groupBy: List<JsonMap> = emptyList(),
    override val having: List<JsonMap> = emptyList(),
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

