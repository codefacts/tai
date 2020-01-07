package tai.orm.delete.impl

import tai.base.JsonMap
import tai.orm.ColumnToColumnMapping
import tai.orm.delete.*
import tai.orm.entity.ForeignColumnMapping
import tai.orm.ex.InvalidStateException
import tai.orm.upsert.TableData
import tai.orm.upsert.TableDataPopulator

class IndirectDependencyDeleteFunctionImpl(
    val relationTable: String,
    val srcColumnMappings: List<ForeignColumnMapping>,
    val dstColumnMappings: List<ForeignColumnMapping>,
    val childEntityDeleteFunction: DeleteFunction,
    val childEntityTableDataPopulator: TableDataPopulator
) : IndirectDependencyDeleteFunction {

    override fun delete(parentTableData: TableData, childEntity: JsonMap, deleteContext: DeleteContext) {

        val relationTableDeleteData = createRelationTableDeleteData(parentTableData, childEntity)

        deleteContext.add(relationTableDeleteData)

        childEntityDeleteFunction.delete(childEntity, deleteContext)
    }

    private fun createRelationTableDeleteData(parentTableData: TableData, childEntity: JsonMap): DeleteData {
        return DeleteData(
            OperationType.DELETE,
            relationTable,
            listOf(),
            createWhereCriteria(parentTableData, childEntityTableDataPopulator.populate(childEntity, false))
        )
    }

    private fun createWhereCriteria(parentTableData: TableData, childTableData: TableData): List<ColumnAndValue> {
        val conditions1 = srcColumnMappings.map { ColumnAndValue(it.srcColumn,
            parentTableData.values[it.dstColumn] ?: throw InvalidStateException("No value exists for column '${it.dstColumn}' in tableData for table '${parentTableData.table}'")
        ) }
        val conditions2 = dstColumnMappings.map { ColumnAndValue(it.srcColumn,
            childTableData.values[it.dstColumn] ?: throw InvalidStateException("No value exists for column '${it.dstColumn}' in tableData for table '${childTableData.table}'")
        ) }
        return conditions1 + conditions2
    }
}