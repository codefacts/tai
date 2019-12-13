package tai.sql

import tai.base.JsonMap
import tai.base.PrimitiveValue
import tai.criteria.operators.JoinType
import tai.criteria.operators.Order
import tai.criteria.ops.valueOf

/**
 * Created by sohan on 4/11/2017.
 */

open class QueryBase(
    open val from: List<FromSpec>,
    open val where: List<JsonMap> = listOf(),
    open val groupBy: List<JsonMap> = listOf(),
    open val having: List<JsonMap> = listOf(),
    open val orderBy: List<OrderBySpec> = listOf(),
    open val pagination: SqlPagination? = null

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QueryBase

        if (from != other.from) return false
        if (where != other.where) return false
        if (groupBy != other.groupBy) return false
        if (having != other.having) return false
        if (orderBy != other.orderBy) return false
        if (pagination != other.pagination) return false

        return true
    }

    override fun hashCode(): Int {
        var result = from.hashCode()
        result = 31 * result + where.hashCode()
        result = 31 * result + groupBy.hashCode()
        result = 31 * result + having.hashCode()
        result = 31 * result + orderBy.hashCode()
        result = 31 * result + (pagination?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "QueryBase(from=$from, where=$where, groupBy=$groupBy, having=$having, orderBy=$orderBy, pagination=$pagination)"
    }
};

data class SqlQuery(
    val selections: List<JsonMap>,
    override val from: List<FromSpec>,
    override val where: List<JsonMap> = listOf(),
    override val groupBy: List<JsonMap> = listOf(),
    override val having: List<JsonMap> = listOf(),
    override val orderBy: List<OrderBySpec> = listOf(),
    override val pagination: SqlPagination? = null
) : QueryBase(from, where, groupBy, having, orderBy, pagination)

data class SqlSelectIntoOp(
    val selections: List<JsonMap>,
    val into: IntoTableSpec,
    override val from: List<FromSpec>,
    override val where: List<JsonMap> = listOf(),
    override val groupBy: List<JsonMap> = listOf(),
    override val having: List<JsonMap> = listOf(),
    override val orderBy: List<OrderBySpec> = listOf(),
    override val pagination: SqlPagination? = null
) : QueryBase(from, where, groupBy, having, orderBy, pagination)

data class SqlInsertIntoOp(
    val into: InsertTableSpec,
    val selections: List<JsonMap>,
    override val from: List<FromSpec>,
    override val where: List<JsonMap> = listOf(),
    override val groupBy: List<JsonMap> = listOf(),
    override val having: List<JsonMap> = listOf(),
    override val orderBy: List<OrderBySpec> = listOf(),
    override val pagination: SqlPagination? = null
) : QueryBase(from, where, groupBy, having, orderBy, pagination)

data class FromSpec(
    val database: String? = null,
    val table: String,
    val alias: String? = null,
    val joins: List<JoinSpec> = listOf()
)

data class JoinSpec(
    val joinType: JoinType = JoinType.JOIN,
    val database: String? = null,
    val table: String,
    val alias: String? = null,
    val joinRules: List<JoinRule>
)

data class JoinRule(
    val from: ColumnSpec,
    val to: ColumnSpec
)

data class GroupBySpec(val columnExpression: JsonMap) {
    constructor(column: String) : this(tai.criteria.ops.column(column))
    constructor(alias: String?, column: String) : this(tai.criteria.ops.column(alias, column))
}

data class OrderBySpec(
    val columnExpression: JsonMap,
    val order: Order
) {
    constructor(column: String) : this(tai.criteria.ops.column(column), Order.ASC)
    constructor(column: String, order: Order) : this(tai.criteria.ops.column(column), order)
    constructor(alias: String?, column: String, order: Order) : this(tai.criteria.ops.column(alias, column), order)
};

data class SqlPagination(
    val offset: Long = 0,
    val size: Int
)

data class ColumnSpec(
    val alias: String?,
    val column: String
) {
    constructor(column: String) : this(null, column)
};

sealed class SqlOperation;

data class SqlInsert(
    val database: String? = null,
    val table: String,
    val data: JsonMap
) : SqlOperation() {
    val sqlOperationType: SqlOperationType = SqlOperationType.INSERT;
}

data class SqlUpdate(
    val database: String?,
    val table: String,
    val data: JsonMap,
    val sqlConditions: Collection<SqlCondition>
) : SqlOperation() {
    val sqlOperationType: SqlOperationType = SqlOperationType.UPDATE;
}

data class SqlDelete(
    val database: String?,
    val table: String,
    val sqlConditions: Collection<SqlCondition>
) : SqlOperation() {
    val sqlOperationType: SqlOperationType = SqlOperationType.DELETE;
}

enum class SqlOperationType {
    INSERT, UPDATE, DELETE
}

data class SqlCondition(
    val column: String,
    val value: PrimitiveValue
);

data class SqlUpdateOp(
    val tables: List<FromSpec>,
    val values: List<ColumnAndValue>,
    val from: List<FromSpec> = listOf(),
    val where: List<JsonMap> = listOf()
);

data class ColumnAndValue(
    val columnExpression: JsonMap,
    val valueExpression: JsonMap
) {
    constructor(alias: String?, column: String, value: PrimitiveValue) : this(
        tai.criteria.ops.column(alias, column),
        valueOf(value)
    )

    constructor(column: String, value: PrimitiveValue) : this(
        tai.criteria.ops.column(column),
        valueOf(value)
    )

    constructor(column: String, valueExpression: JsonMap) : this(
        tai.criteria.ops.column(column),
        valueExpression
    )
}

data class SqlDeleteOp(
    val database: String? = null,
    val table: String,
    val where: List<JsonMap> = listOf()
);

data class IntoTableSpec(
    val table: String,
    val database: String?
);

data class InsertTableSpec(
    val database: String? = null,
    val table: String,
    val columns: List<String>
) {
    constructor(table: String, columns: List<String>) : this(null, table, columns)
}