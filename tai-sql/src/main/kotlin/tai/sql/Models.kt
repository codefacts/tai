package tai.sql

import tai.base.JsonMap
import tai.criteria.operators.JoinType
import tai.criteria.operators.Order

/**
 * Created by sohan on 4/11/2017.
 */
data class SqlQuery(
    val selections: List<JsonMap>,
    val from: List<FromSpec>,
    val where: List<JsonMap> = listOf(),
    val groupBy: List<AliasAndColumn> = listOf(),
    val having: List<JsonMap> = listOf(),
    val orderBy: List<OrderBySpec> = listOf(),
    val pagination: SqlPagination? = null
)

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
    val pagination: AliasAndColumn,
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
    val table: String,
    val data: JsonMap
) : SqlOperation() {
    val sqlOperationType: SqlOperationType = SqlOperationType.INSERT;
}

data class SqlUpdate(
    val table: String,
    val data: JsonMap,
    val sqlConditions: Collection<SqlCondition>
) : SqlOperation() {
    val sqlOperationType: SqlOperationType = SqlOperationType.UPDATE;
}

data class SqlDelete(
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
    val from: List<FromSpec>,
    val where: List<JsonMap> = listOf()
);

data class SqlDeleteOp(
    val database: String? = null,
    val table: String,
    val where: List<JsonMap> = listOf()
);

data class SqlSelectIntoOp(
    val selections: List<JsonMap>,
    val into: IntoTableSpec,
    val from: List<FromSpec>,
    val where: List<JsonMap> = listOf(),
    val groupBy: List<AliasAndColumn> = listOf(),
    val having: List<JsonMap> = listOf(),
    val orderBy: List<OrderBySpec> = listOf(),
    val pagination: SqlPagination? = null
);

data class IntoTableSpec(
    val table: String,
    val database: String?
);

data class SqlInsertIntoOp(
    val into: InsertTableSpec,
    val selections: List<JsonMap>,
    val from: List<FromSpec>,
    val where: List<JsonMap> = listOf(),
    val groupBy: List<AliasAndColumn> = listOf(),
    val having: List<JsonMap> = listOf(),
    val orderBy: List<OrderBySpec> = listOf(),
    val pagination: SqlPagination? = null
);

data class InsertTableSpec(
    val database: String? = null,
    val table: String,
    val columns: List<String>
) {
    constructor(table: String, columns: List<String>) : this(null, table, columns)
}