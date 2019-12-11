package tai.orm.entity.core.columnmapping.impl

import tai.orm.entity.core.columnmapping.DirectRelationMappingOptions
import tai.orm.entity.core.columnmapping.DirectRelationMappingOptions.LoadAndDelete
import tai.orm.entity.core.columnmapping.RelationMappingOptions.CascadeDelete
import tai.orm.entity.core.columnmapping.RelationMappingOptions.CascadeUpsert

/**
 * Created by sohan on 4/18/2017.
 */
class DirectRelationMappingOptionsImpl @JvmOverloads constructor(
    override val cascadeUpsert: CascadeUpsert = CascadeUpsert.NO,
    override val cascadeDelete: CascadeDelete = CascadeDelete.NO,
    override val isMandatory: Boolean = false,
    override val loadAndDelete: LoadAndDelete = LoadAndDelete.NO_OPERATION
) : DirectRelationMappingOptions