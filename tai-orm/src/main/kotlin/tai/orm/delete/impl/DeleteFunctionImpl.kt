package tai.orm.delete.impl

import tai.base.JsonMap
import tai.orm.delete.*
import tai.orm.ex.InvalidStateException
import tai.orm.upsert.TableData
import tai.orm.upsert.TableDataPopulator
import tai.orm.upsert.UpsertUtils

class DeleteFunctionImpl(
    val tableDataPopulator: TableDataPopulator,
    val directDependencyHandlers: List<DirectDependencyHandler>,
    val indirectDependencyHandlers: List<IndirectDependencyHandler>,
    val virtualDependencyHandlers: List<VirtualDependencyHandler>
): DeleteFunction {

    override fun delete(entity: JsonMap, deleteContext: DeleteContext): JsonMap {

        val tableData = tableDataPopulator.populate(entity, false)

        virtualDependencyHandlers.forEach { (field, deleteFun) ->
            val childValue = entity[field] ?: return@forEach
            UpsertUtils.traverseEntityMapOrList(childValue) { childEntity ->
                deleteFun.delete(childEntity, deleteContext)
            }
        }

        indirectDependencyHandlers.forEach { (field, deleteFun) ->
            val childValue = entity[field] ?: return@forEach
            UpsertUtils.traverseEntityMapOrList(childValue) { childEntity ->
                deleteFun.delete(tableData, childEntity, deleteContext)
            }
        }

        deleteContext.add(toDeleteDate(tableData))

        directDependencyHandlers.forEach { (field, deleteFun) ->
            val childEntity = entity[field] as JsonMap? ?: return@forEach
            deleteFun.delete(childEntity, deleteContext)
        }

        return entity
    }

    private fun toDeleteDate(tableData: TableData): DeleteData {
        val values = tableData.values
        return DeleteData(
            OperationType.DELETE,
            tableData.table,
            listOf(),
            tableData.primaryColumns.map { col -> ColumnAndValue(col,
                values[col] ?: throw InvalidStateException("No value given for primary column '$col' in table data for table '${tableData.table}'")
            ) }
        )
    }
}