package tai.orm.delete

import tai.base.PrimitiveValue

data class VirtualDependencyHandler(
    val field: String,
    val virtualDependencyDeleteFunction: DeleteFunction
)

data class DirectDependencyHandler(
    val field: String,
    val directDependencyDeleteFunction: DeleteFunction
)

data class IndirectDependencyHandler(
    val field: String,
    val indirectDependencyDeleteFunction: IndirectDependencyDeleteFunction
)

data class DeleteData(
    val operationType: OperationType,
    val table: String,
    val updateValues: List<ColumnAndValue>,
    val whereCriteria: List<ColumnAndValue>
)

data class ColumnAndValue(
    val column: String,
    val value: PrimitiveValue
)

enum class OperationType {UPDATE, DELETE}