package tai.orm.entity.core

import java.util.*

/**
 * Created by Jango on 2017-01-08.
 */
data class Field(
    val name: String,
    val javaType: JavaType = JavaType.DOUBLE,
    val relationship: Relationship? = null
)