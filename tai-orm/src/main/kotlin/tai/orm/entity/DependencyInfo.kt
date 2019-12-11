package tai.orm.entity

import tai.orm.entity.core.Field
import tai.orm.entity.core.columnmapping.RelationMapping
import java.util.*

/**
 * Created by sohan on 3/17/2017.
 */
class DependencyInfo(field: Field, relationMapping: RelationMapping) {
    val field: Field
    val relationMapping: RelationMapping

    init {
        Objects.requireNonNull(field)
        Objects.requireNonNull(relationMapping)
        this.field = field
        this.relationMapping = relationMapping
    }
}