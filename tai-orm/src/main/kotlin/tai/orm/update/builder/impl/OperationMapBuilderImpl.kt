package tai.orm.update.builder.impl

import com.google.common.collect.ImmutableList
import tai.orm.delete.DeleteFunction
import tai.orm.delete.DirectDependencyHandler
import tai.orm.delete.IndirectDependencyHandler
import tai.orm.delete.VirtualDependencyHandler
import tai.orm.delete.impl.DeleteFunctionImpl
import tai.orm.delete.impl.IndirectDependencyDeleteFunctionImpl
import tai.orm.entity.Entity
import tai.orm.entity.EntityMappingHelper
import tai.orm.entity.columnmapping.DirectRelationMapping
import tai.orm.entity.columnmapping.IndirectRelationMapping
import tai.orm.entity.columnmapping.VirtualRelationMapping
import tai.orm.update.OperationHolder
import tai.orm.update.OperationMap
import tai.orm.update.builder.OperationMapBuilder
import tai.orm.upsert.*
import tai.orm.upsert.impl.*

class OperationMapBuilderImpl(
    val helper: EntityMappingHelper,
    val isNewKey: String
) : OperationMapBuilder {

    override fun build(entities: Collection<Entity>): OperationMap {
        val context = OperationMapBuilderContextImpl()
        entities.forEach { entity -> createOperation(entity.name, context) }
        return context.build()
    }

    private fun createOperation(entityName: String, context: OperationMapBuilderContextImpl): OperationHolder {

        if (context.containsHolder(entityName)) {
            return context.get(entityName)
        }

        context.putEmpty(entityName)

        val operationHolder = createOperationHolder(entityName, context)

        context.put(entityName, operationHolder)
        return operationHolder
    }

    private fun createOperationHolder(entityName: String, context: OperationMapBuilderContextImpl): OperationHolder {

        val entity = helper.getEntity(entityName)

        val tableDataPopulator = createTableDataPopulator(entity)

        val directDependenciesBuilder = ImmutableList.builder<DirectDependency>()
        val belongsTosBuilder = ImmutableList.builder<BelongsTo>()
        val indirectDependenciesBuilder = ImmutableList.builder<IndirectDependency>()

        val directBuilder = ImmutableList.builder<DirectDependencyHandler>()
        val indirectBuilder = ImmutableList.builder<IndirectDependencyHandler>()
        val virtualBuilder = ImmutableList.builder<VirtualDependencyHandler>()

        entity.dbMapping.relationMappings.forEach { mapping ->
            when (mapping) {
                is DirectRelationMapping -> {
                    directDependenciesBuilder.add(
                        DirectDependency(
                            mapping.field,
                            DirectDependencyHandlerImpl(
                                createOperation(mapping.referencingEntity, context).upsertFunction
                            ),
                            DependencyColumnValuePopulatorImpl(
                                mapping.foreignColumnMappingList
                            )
                        )
                    )
                    directBuilder.add(
                        DirectDependencyHandler(
                            mapping.field,
                            createOperation(mapping.referencingEntity, context).deleteFunction
                        )
                    )
                }
                is VirtualRelationMapping -> {
                    belongsTosBuilder.add(
                        BelongsTo(
                            mapping.field,
                            belongToHandler = BelongToHandlerImpl(
                                createOperation(mapping.referencingEntity, context).upsertFunction
                            ),
                            dependencyColumnValuePopulator = DependencyColumnValuePopulatorImpl(
                                mapping.foreignColumnMappingList
                            )
                        )
                    )
                    virtualBuilder.add(
                        VirtualDependencyHandler(mapping.field, createOperation(mapping.referencingEntity, context).deleteFunction)
                    )
                }
                is IndirectRelationMapping -> {
                    indirectDependenciesBuilder.add(
                        IndirectDependency(
                            mapping.field,
                            indirectDependencyHandler = IndirectDependencyHandlerImpl(
                                relationTableUpserFunction = RelationTableUpsertFunctionImpl(
                                    RelationTableDataPopulatorImpl(
                                        mapping.relationTable,
                                        srcMappings = mapping.srcForeignColumnMappingList,
                                        dstMappings = mapping.dstForeignColumnMappingList
                                    )
                                ),
                                childUpsertFunction = createOperation(mapping.referencingEntity, context).upsertFunction
                            )
                        )
                    )
                    indirectBuilder.add(
                        IndirectDependencyHandler(
                            mapping.field,
                            IndirectDependencyDeleteFunctionImpl(
                                mapping.relationTable,
                                srcColumnMappings = mapping.srcForeignColumnMappingList,
                                dstColumnMappings = mapping.dstForeignColumnMappingList,
                                childEntityDeleteFunction = createOperation(mapping.referencingEntity, context).deleteFunction,
                                childEntityTableDataPopulator = createTableDataPopulator(helper.getEntity(mapping.referencingEntity))
                            )
                        )
                    )
                }
            }
        }

        return OperationHolder(
            upsertFunction = UpsertFunctionImpl(
                entity = entity.name,
                isNewKey = isNewKey,
                tableDataPopulator = tableDataPopulator,
                directDependencies = directDependenciesBuilder.build(),
                belongsTos = belongsTosBuilder.build(),
                indirectDependencies = indirectDependenciesBuilder.build()
            ),
            deleteFunction = DeleteFunctionImpl(
                tableDataPopulator = tableDataPopulator,
                indirectDependencyHandlers = indirectBuilder.build(),
                directDependencyHandlers = directBuilder.build(),
                virtualDependencyHandlers = virtualBuilder.build()
            )
        )
    }

    private fun createTableDataPopulator(entity: Entity): TableDataPopulator {
        return TableDataPopulatorImpl(
            entity.dbMapping.table,
            entity.dbMapping.primaryColumn,
            fieldToColumnMappings = entity.dbMapping.columnMappings.map { FieldToColumnMapping(it.field, it.column) }
        )
    }
}