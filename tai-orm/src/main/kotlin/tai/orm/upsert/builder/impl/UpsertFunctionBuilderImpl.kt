package tai.orm.upsert.builder.impl

import tai.base.JsonMap
import tai.orm.OrmUtils
import tai.orm.OrmUtils.cast
import tai.orm.core.BuilderContext
import tai.orm.entity.DbMapping
import tai.orm.entity.EntityMappingHelper
import tai.orm.entity.ForeignColumnMapping
import tai.orm.entity.RelationType
import tai.orm.entity.columnmapping.*
import tai.orm.upsert.*
import tai.orm.upsert.UpsertUtils.Companion.getRelationMappingsForUpsert
import tai.orm.upsert.builder.UpsertFunctionBuilder
import tai.orm.upsert.builder.ex.UpsertFunctionBuilderException
import tai.orm.upsert.impl.*
import java.util.*
import java.util.stream.Collectors

/**
 * Created by Jango on 2017-01-21.
 */
class UpsertFunctionBuilderImpl(val helper: EntityMappingHelper, val isNewKey: String) :
    UpsertFunctionBuilder {

    override fun build(entity: String, context: BuilderContext<UpsertFunction>): UpsertFunction {
        return QQ(context).build(entity)
    }

    private inner class QQ(val context: BuilderContext<UpsertFunction>) {

        fun build(entity: String): UpsertFunction {

            if (context.contains(entity)) {
                return context[entity]
            }
            if (context.isEmpty(entity)) {
                return ProxyUpsertFunctionImpl(entity, context)
            }
            context.putEmpty(entity)
            val upsertFunction = buildUpsertFunction(entity)
            context.put(entity, upsertFunction)
            return upsertFunction
        }

        private fun buildUpsertFunction(entity: String): UpsertFunction {
            val dbMapping = helper.getDbMapping(entity)
            val tableDataPopulator = createTableDataPopulator(dbMapping)
            val directDependencies: MutableList<DirectDependency> =
                ArrayList()
            val indirectDependencies: MutableList<IndirectDependency> =
                ArrayList()
            val belongsTos: MutableList<BelongsTo> = ArrayList()

            getRelationMappingsForUpsert(helper.getEntity(entity), helper)
                .forEach { dbColumnMapping: RelationMapping ->
                    when (dbColumnMapping.columnType) {
                        RelationType.DIRECT -> {
                            if (!helper.getField(entity, dbColumnMapping.field).relationship!!.options.cascadeUpsert) {
                                directDependencies.add(
                                    makeTableDataPopulatingDirectMapping(
                                        dbColumnMapping as DirectRelationMapping
                                    )
                                )
                                return@forEach
                            }
                            directDependencies.add(
                                makeDirectMapping(
                                    dbColumnMapping as DirectRelationMapping
                                )
                            )
                            return@forEach
                        }
                        RelationType.INDIRECT -> {
                            indirectDependencies.add(
                                makeIndirectDepedency(
                                    dbColumnMapping as IndirectRelationMapping
                                )
                            )
                            return@forEach
                        }
                        RelationType.VIRTUAL -> {
                            belongsTos.add(
                                makeBelongTo(
                                    dbColumnMapping as VirtualRelationMapping
                                )
                            )
                            return@forEach
                        }
                        else -> {
                            throw UpsertFunctionBuilderException("Invalid columnType '" + dbColumnMapping.columnType + "' in '" + entity + "." + dbColumnMapping.field + "'")
                        }
                    }
                }
            return UpsertFunctionImpl(
                entity,
                isNewKey,
                tableDataPopulator,
                directDependencies,
                indirectDependencies,
                belongsTos
            )
        }

        private fun createTableDataPopulator(dbMapping: DbMapping): TableDataPopulatorImpl {
            val list = dbMapping.columnMappings.stream()
                .map { dbColumnMapping: ColumnMapping ->
                    FieldToColumnMapping(
                        dbColumnMapping.field,
                        cast<ColumnMapping>(dbColumnMapping).column
                    )
                }
                .collect(Collectors.toList())
            return TableDataPopulatorImpl(
                dbMapping.table,
                dbMapping.primaryColumn,
                list
            )
        }

        private fun makeTableDataPopulatingDirectMapping(mapping: DirectRelationMapping): DirectDependency {
            val foreignColumnMappings =
                mapping.foreignColumnMappingList.stream()
                    .map { (srcColumn, dstColumn) ->
                        ForeignColumnMapping(
                            srcColumn,
                            dstColumn
                        )
                    }.collect(Collectors.toList())
            return DirectDependency(
                mapping.field,
                TableDataPopulatingDirectDependencyHandlerImpl(
                    createTableDataPopulator(helper.getDbMapping(mapping.referencingEntity))
                ),
                DependencyColumnValuePopulatorImpl(
                    foreignColumnMappings
                )
            )
        }

        private fun makeDirectMapping(mapping: DirectRelationMapping): DirectDependency {
            val foreignColumnMappings =
                mapping.foreignColumnMappingList.stream()
                    .map { (srcColumn, dstColumn) ->
                        ForeignColumnMapping(
                            srcColumn,
                            dstColumn
                        )
                    }.collect(Collectors.toList())
            return DirectDependency(
                mapping.field,
                DirectDependencyHandlerImpl(
                    createUpsertFunction(mapping.referencingEntity)
                ),
                DependencyColumnValuePopulatorImpl(
                    foreignColumnMappings
                )
            )
        }

        private fun makeIndirectDepedency(indirectRelationMapping: IndirectRelationMapping): IndirectDependency {
            val srcMappings =
                indirectRelationMapping.srcForeignColumnMappingList.stream()
                    .map { (srcColumn, dstColumn) ->
                        ForeignColumnMapping(
                            srcColumn,
                            dstColumn
                        )
                    }
                    .collect(Collectors.toList())
            val dstMappings =
                indirectRelationMapping.dstForeignColumnMappingList.stream()
                    .map { (srcColumn, dstColumn) ->
                        ForeignColumnMapping(
                            srcColumn,
                            dstColumn
                        )
                    }
                    .collect(Collectors.toList())
            return IndirectDependency(
                indirectRelationMapping.field,
                IndirectDependencyHandlerImpl(
                    RelationTableUpsertFunctionImpl(
                        RelationTableDataPopulatorImpl(
                            indirectRelationMapping.relationTable,
                            srcMappings,
                            dstMappings
                        )
                    ),
                    createUpsertFunction(indirectRelationMapping.referencingEntity)
                )
            )
        }

        private fun createUpsertFunction(referencingEntity: String): UpsertFunction {
            return build(referencingEntity)
        }

        private fun makeBelongTo(mapping: VirtualRelationMapping): BelongsTo {
            val foreignColumnMappings =
                mapping.foreignColumnMappingList.stream()
                    .map { (srcColumn, dstColumn) ->
                        ForeignColumnMapping(
                            srcColumn,
                            dstColumn
                        )
                    }
                    .collect(Collectors.toList())
            return BelongsTo(
                mapping.field,
                BelongToHandlerImpl(
                    createUpsertFunction(mapping.referencingEntity)
                ),
                DependencyColumnValuePopulatorImpl(
                    foreignColumnMappings
                )
            )
        }
    }

    private class ProxyUpsertFunctionImpl(
        val referencingEntity: String,
        val functionMap: BuilderContext<UpsertFunction>
    ) : UpsertFunction {

        override fun upsert(
            jsonObject: JsonMap,
            upsertContext: UpsertContext
        ): TableData {
            return functionMap[referencingEntity].upsert(jsonObject, upsertContext)
        }
    }
}