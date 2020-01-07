package tai.orm.update.impl

import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.google.common.collect.Streams
import tai.orm.DeleteParam
import tai.orm.ExecuteParam
import tai.orm.update.UpdateExecutor
import tai.orm.UpsertParam
import tai.orm.delete.DeleteData
import tai.orm.delete.OperationType
import tai.orm.delete.impl.DeleteContextImpl
import tai.orm.ex.InvalidStateException
import tai.orm.update.OperationMap
import tai.orm.upsert.TableData
import tai.orm.upsert.impl.UpsertContextImpl
import tai.sql.*
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.streams.toList

class UpdateExecutorImpl(
    val operationMap: OperationMap,
    val baseSqlDB: BaseSqlDB
) : UpdateExecutor {

    override suspend fun upsert(param: UpsertParam): UpsertParam {

        baseSqlDB.executeAll(
            toSqlOperations(param)
        )
        return param.copy(jsonObject = param.jsonObject)
    }

    override suspend fun delete(param: DeleteParam): DeleteParam {
        baseSqlDB.executeAll(
            toSqlOperations(param)
        )
        return param.copy(
            jsonObject = param.jsonObject
        )
    }

    override suspend fun executeAll(params: Stream<ExecuteParam>): Stream<ExecuteParam> {

        val builder = Stream.builder<ExecuteParam>()

        baseSqlDB.executeAll(
            params.peek { builder.accept(it) }.flatMap { param ->
                when(param.operationType) {
                    tai.orm.OperationType.UPSERT -> {
                        toSqlOperations(
                            UpsertParam(
                            entity = param.entity, jsonObject = param.jsonObject
                        ))
                    }
                    tai.orm.OperationType.DELETE -> {
                        toSqlOperations(
                            DeleteParam(
                                entity = param.entity, jsonObject = param.jsonObject
                            )
                        )
                    }
                }
            }
        )
        return builder.build()
    }

    private fun toSqlOperations(param: UpsertParam): Stream<SqlOperation> {
        val tableDataMap = Maps.newLinkedHashMap<String, TableData>()
        val entity = operationMap.get(param.entity).upsertFunction.upsert(
            param.jsonObject, UpsertContextImpl(tableDataMap)
        )

        return tableDataMap.values.map { tableData ->
            if (tableData.isNew) {
                SqlInsert(
                    table = tableData.table,
                    data = tableData.values
                )
            } else {
                SqlUpdate(
                    table = tableData.table,
                    data = tableData.values,
                    sqlConditions = tableData.primaryColumns.map { primaryColumn ->
                        SqlCondition(
                            primaryColumn, tableData.values[primaryColumn]
                                ?: throw InvalidStateException("No value exists for primary column '$primaryColumn' in tableData for table '${tableData.table}'")
                        )
                    }
                )
            }
        }.stream()
    }

    private fun toSqlOperations(param: DeleteParam): Stream<SqlOperation> {
        val deleteDateSet = Sets.newLinkedHashSet<DeleteData>()
        val entity = operationMap.get(param.entity).deleteFunction.delete(
            param.jsonObject, DeleteContextImpl(deleteDateSet)
        )

        return deleteDateSet.stream().map { deleteData ->
            when (deleteData.operationType) {
                OperationType.UPDATE -> SqlUpdate(
                    table = deleteData.table,
                    data = deleteData.updateValues.asSequence()
                        .map { it.column to it.value }.toMap(),
                    sqlConditions = deleteData.whereCriteria.map {
                        SqlCondition(it.column, it.value)
                    }
                )
                OperationType.DELETE -> {
                    SqlDelete(
                        table = deleteData.table,
                        sqlConditions = deleteData.whereCriteria.map {
                            SqlCondition(it.column, it.value)
                        }
                    )
                }
                else -> throw InvalidStateException("Invalid operation type '${deleteData.operationType}' in deleteData for table '${deleteData.table}'")
            }
        }
    }
}