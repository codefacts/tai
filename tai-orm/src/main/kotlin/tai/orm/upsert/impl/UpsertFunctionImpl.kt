package tai.orm.upsert.impl

import tai.base.JsonMap
import tai.base.MutableJsonMap
import tai.orm.upsert.*
import tai.orm.upsert.UpsertUtils.Companion.isNew
import tai.orm.upsert.UpsertUtils.Companion.toTableAndPrimaryColumnsKey
import kotlin.collections.LinkedHashMap

/**
 * Created by Jango on 2017-01-09.
 */
class UpsertFunctionImpl(
    val entity: String,
    val isNewKey: String,
    val tableDataPopulator: TableDataPopulator,
    val directDependencies: List<DirectDependency>,
    val indirectDependencies: List<IndirectDependency>,
    val belongsTos: List<BelongsTo>
) : UpsertFunction {

    override fun upsert(
        jsonObject: JsonMap,
        upsertContext: UpsertContext
    ): TableData {

        return Inserter(jsonObject, upsertContext).insert()
    }

    private inner class Inserter(
        val entity: JsonMap,
        val context: UpsertContext
    ) {
        fun insert(): TableData {

            val tableData = tableData()
            val tableValues = tableData.values
            val tableAndPrimaryColumnsKey = toTableAndPrimaryColumnsKey(
                tableData.table, tableData.primaryColumns, tableValues
            )

            context.putOrMerge(
                tableAndPrimaryColumnsKey,
                tableData
            )

            for (indirectDependency in indirectDependencies) {
                handleIndirectDependency(
                    indirectDependency,
                    tableData
                )
            }

            for (belongsTo in belongsTos) {
                handleBelongTo(belongsTo, tableValues)
            }

            return context[tableAndPrimaryColumnsKey]
        }

        private fun tableData(): TableData {

            val (table, primaryColumns, tableValues, isNew) = tableDataPopulator.populate(entity, isNew(entity, isNewKey))

            val mutableMap: MutableJsonMap = LinkedHashMap(tableValues)

            for ((field, dependencyHandler, dependencyColumnValuePopulator) in directDependencies) {
                val jsonObject = this.entity[field] as JsonMap? ?: continue
                val dependencyColumnValues =
                    dependencyColumnValuePopulator
                        .populate(
                            dependencyHandler
                                .upsert(
                                    jsonObject,
                                    context
                                )
                                .values
                        )
                mutableMap.putAll(dependencyColumnValues)
            }
            return TableData(
                table,
                primaryColumns,
                mutableMap,
                isNew
            )
        }

        private fun handleBelongTo(
            belongsTo: BelongsTo,
            tableValues: JsonMap
        ) {
            val field = belongsTo.field
            val value = entity[field] ?: return
            UpsertUtils.traverseJsonTree(value) { jsonObject: JsonMap ->
                handleBelongToJsonObject(
                    belongsTo,
                    tableValues,
                    jsonObject
                )
            }
        }

        private fun handleBelongToJsonObject(
            belongsTo: BelongsTo,
            jsonObject: JsonMap,
            value: JsonMap
        ) {
            belongsTo
                .belongToHandler
                .upsert(
                    value,
                    belongsTo
                        .dependencyColumnValuePopulator
                        .populate(jsonObject),
                    context
                )
        }

        private fun handleIndirectDependency(
            indirectDependency: IndirectDependency, tableData: TableData
        ) {
            val field = indirectDependency.field
            val value = entity[field] ?: return
            UpsertUtils.traverseJsonTree(value) { jsonObject: JsonMap ->
                handleIndirectJsonObject(
                    indirectDependency,
                    tableData,
                    jsonObject
                )
            }
        }

        private fun handleIndirectJsonObject(
            indirectDependency: IndirectDependency,
            tableData: TableData,
            jsonObject: JsonMap
        ): JsonMap {
            return indirectDependency
                .indirectDependencyHandler
                .upsert(
                    tableData,
                    jsonObject,
                    context
                )
                .values
        }

    }
}