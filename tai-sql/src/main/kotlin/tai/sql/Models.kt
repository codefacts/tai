package tai.sql

import tai.base.JsonMap
import tai.criteria.operators.JoinType
import tai.criteria.operators.Order

/**
 * Created by sohan on 4/11/2017.
 */

open class QueryBase(
    open val from: List<FromSpec>,
    open val where: List<JsonMap> = listOf(),
    open val groupBy: List<AliasAndColumn> = listOf(),
    open val having: List<JsonMap> = listOf(),
    open val orderBy: List<OrderBySpec> = listOf(),
    open val pagination: SqlPagination? = null
);

data class SqlQuery(
    val selections: List<JsonMap>,
    override val from: List<FromSpec>,
    override val where: List<JsonMap> = listOf(),
    override val groupBy: List<AliasAndColumn> = listOf(),
    override val having: List<JsonMap> = listOf(),
    override val orderBy: List<OrderBySpec> = listOf(),
    override val pagination: SqlPagination? = null
) : QueryBase(from, where, groupBy, having, orderBy, pagination)

data class SqlSelectIntoOp(
    val selections: List<JsonMap>,
    val into: IntoTableSpec,
    override val from: List<FromSpec>,
    override val where: List<JsonMap> = listOf(),
    override val groupBy: List<AliasAndColumn> = listOf(),
    override val having: List<JsonMap> = listOf(),
    override val orderBy: List<OrderBySpec> = listOf(),
    override val pagination: SqlPagination? = null
) : QueryBase(from, where, groupBy, having, orderBy, pagination)

data class SqlInsertIntoOp(
    val into: InsertTableSpec,
    val selections: List<JsonMap>,
    override val from: List<FromSpec>,
    override val where: List<JsonMap> = listOf(),
    override val groupBy: List<AliasAndColumn> = listOf(),
    override val having: List<JsonMap> = listOf(),
    override val orderBy: List<OrderBySpec> = listOf(),
    override val pagination: SqlPagination? = null
) : QueryBase(from, where, groupBy, having, orderBy, pagination)

data class FromSpec(
    val database: String?,
    val table: String,
    val alias: String?,
    val joins: List<JoinSpec> = listOf()
) {
    constructor(table: String, alias: String) : this(null, table, alias)
    constructor(table: String) : this(null, table, null)
}

data class JoinSpec(
    val toAlias: String,
    val joinType: JoinType = JoinType.JOIN,
    val database: String? = null,
    val table: String,
    val alias: String? = null,
    val joinRules: List<JoinRule>
)

data class JoinRule(
    val fromColumn: AliasAndColumn,
    val toColumn: AliasAndColumn
)

data class OrderBySpec(
    val alias: String?,
    val column: String,
    val order: Order
) {
    constructor(column: String, order: Order = Order.ASC) : this(null, column, order)
    constructor(column: String) : this(null, column, Order.ASC)
};

data class SqlPagination(
    val offset: Long = 0,
    val size: Int
)

data class AliasAndColumn(
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
    val value: Any
);

data class SqlUpdateOp(
    val database: String? = null,
    val table: String,
    val values: List<ColumnAndValue>,
    val from: List<FromSpec>? = null,
    val where: List<JsonMap> = listOf()
);

data class ColumnAndValue(
    val column: String,
    val valueExpression: JsonMap
);

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