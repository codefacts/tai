package tai.orm.entity.core.columnmapping.impl

import tai.orm.entity.core.columnmapping.RelationMappingOptions
import tai.orm.entity.core.columnmapping.RelationMappingOptions.CascadeDelete
import tai.orm.entity.core.columnmapping.RelationMappingOptions.CascadeUpsert

/**
 * Created by sohan on 4/18/2017.
 */
class RelationMappingOptionsImpl @JvmOverloads constructor(
    override val cascadeUpsert: CascadeUpsert = CascadeUpsert.NO,
    override val cascadeDelete: CascadeDelete = CascadeDelete.NO,
    override val isMandatory: Boolean = false
) : RelationMappingOptions