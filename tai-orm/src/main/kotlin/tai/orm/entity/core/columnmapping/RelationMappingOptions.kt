package tai.orm.entity.core.columnmapping

/**
 * Created by sohan on 4/18/2017.
 */
interface RelationMappingOptions {
    val cascadeUpsert: CascadeUpsert?
    val cascadeDelete: CascadeDelete?
    val isMandatory: Boolean

    enum class CascadeUpsert {
        YES, NO
    }

    enum class CascadeDelete {
        YES, NO
    }
}