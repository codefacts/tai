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
    open val from: Collection<FromSpec>,
    open val where: Collection<JsonMap> = listOf(),
    open val groupBy: Collection<ColumnSpec> = listOf(),
    open val having: Collection<JsonMap> = listOf(),
    open val orderBy: Collection<OrderBySpec> = listOf(),
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
    val selections: Collection<JsonMap>,
    override val from: Collection<FromSpec>,
    override val where: Collection<JsonMap> = listOf(),
    override val groupBy: Collection<ColumnSpec> = listOf(),
    override val having: Collection<JsonMap> = listOf(),
    override val orderBy: Collection<OrderBySpec> = listOf(),
    override val pagination: SqlPagination? = null
) : QueryBase(from, where, groupBy, having, orderBy, pagination)

data class SqlSelectIntoOp(
    val selections: Collection<JsonMap>,
    val into: IntoTableSpec,
    override val from: Collection<FromSpec>,
    override val where: Collection<JsonMap> = listOf(),
    override val groupBy: Collection<ColumnSpec> = listOf(),
    override val having: Collection<JsonMap> = listOf(),
    override val orderBy: Collection<OrderBySpec> = listOf(),
    override val pagination: SqlPagination? = null
) : QueryBase(from, where, groupBy, having, orderBy, pagination)

data class SqlInsertIntoOp(
    val into: InsertTableSpec,
    val selections: Collection<JsonMap>,
    override val from: Collection<FromSpec>,
    override val where: Collection<JsonMap> = listOf(),
    override val groupBy: Collection<ColumnSpec> = listOf(),
    override val having: Collection<JsonMap> = listOf(),
    override val orderBy: Collection<OrderBySpec> = listOf(),
    override val pagination: SqlPagination? = null
) : QueryBase(from, where, groupBy, having, orderBy, pagination)

data class FromSpec(
    val database: String? = null,
    val table: String,
    val alias: String? = null,
    val joins: Collection<JoinSpec> = listOf()
)

data class JoinSpec(
    val joinType: JoinType = JoinType.JOIN,
    val database: String? = null,
    val table: String,
    val alias: String? = null,
    val joinRules: Collection<JoinRule>
)

data class JoinRule(
    val from: ColumnSpec,
    val to: ColumnSpec
)

data class OrderBySpec(
    val alias: String? = null,
    val column: String,
    val order: Order = Order.ASC
) {
    constructor(column: String, order: Order): this(null, column, order)
}

data class SqlPagination(
    val offset: Long = 0,
    val size: Int,
    val paginationColumnSpec: ColumnSpec? = null
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
    val tables: Collection<FromSpec>,
    val values: Collection<ColumnAndValue>,
    val from: Collection<FromSpec> = listOf(),
    val where: Collection<JsonMap> = listOf()
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
    val where: Collection<JsonMap> = listOf()
);

data class IntoTableSpec(
    val table: String,
    val database: String?
);

data class InsertTableSpec(
    val database: String? = null,
    val table: String,
    val columns: Collection<String>
) {
    constructor(table: String, columns: Collection<String>) : this(null, table, columns)
}