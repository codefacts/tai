package tai.sql

import tai.base.JsonMap
import tai.criteria.CriOperation

/**
 * Created by sohan on 4/11/2017.
 */
data class SqlQuery(
    val selections: Collection<CriOperation>,
    val from: Collection<TableAliasPair>,
    val joins: Collection<JoinData> = listOf(),
    val where: Collection<CriOperation> = listOf(),
    val orderBy: Collection<CriOperation> = listOf(),
    val groupBy: Collection<CriOperation> = listOf(),
    val having: Collection<CriOperation> = listOf(),
    val pagination: SqlPagination?
)

data class TableAliasPair(
    val table: String,
    val alias: String
)

data class JoinData(
    val parentAlias: String,
    val joinType: JoinType,
    val table: String,
    val alias: String,
    val columnToColumnMappings: List<ColumnToColumnMapping>
)

enum class JoinType() {
    INNER_JOIN,
    LEFT_JOIN,
    RIGHT_JOIN,
    FULL_JOIN
}

data class ColumnToColumnMapping(
    val srcColumn: String,
    val dstColumn: String
)

data class SqlPagination(
    val paginationColumn: ColumnAliasPair,
    val offset: Long = 0,
    val size: Int
)

data class ColumnAliasPair(
    val column: String,
    val alias: String
);

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