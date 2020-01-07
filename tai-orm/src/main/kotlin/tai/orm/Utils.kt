package tai.orm

import tai.base.JsonMap
import tai.criteria.operators.arg_
import tai.criteria.operators.op_
import tai.orm.core.FieldExpression

const val field_ = "field"
const val translatedTo_ = "translatedTo"

class Utils {
    companion object {

        fun not(value: Boolean) = !value

        fun <T> cast(t: Any): T = t as T
    }
}

fun field(fieldExp: FieldExpression): JsonMap {
    return mapOf(
        op_ to field_,
        arg_ to fieldExp
    )
}