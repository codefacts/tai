package tai.orm.entity.core.columnmapping

/**
 * Created by sohan on 4/18/2017.
 */
interface DirectRelationMappingOptions : RelationMappingOptions {
    abstract override val cascadeUpsert: RelationMappingOptions.CascadeUpsert?
    abstract override val cascadeDelete: RelationMappingOptions.CascadeDelete?
    val loadAndDelete: LoadAndDelete?

    enum class LoadAndDelete {
        LOAD_AND_DELETE, SET_TO_NULL, NO_OPERATION
    }
}