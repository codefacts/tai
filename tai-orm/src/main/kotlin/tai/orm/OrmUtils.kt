package tai.orm

import tai.base.JsonMap
import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverter
import tai.criteria.CriteriaToTextConverterImpl
import tai.criteria.operators.OPERATION_MAP
import tai.criteria.operators.arg_
import tai.criteria.operators.op_
import tai.orm.core.FieldExpression
import tai.orm.query.impl.FieldExpOperator

const val field_ = "field"
const val translatedTo_ = "translatedTo"

object OrmUtils {
    fun not(value: Boolean) = !value
    fun <T> cast(t: Any): T = t as T
}

fun field(fieldExp: FieldExpression): JsonMap {
    return mapOf(
        op_ to field_,
        arg_ to fieldExp
    )
}

fun field(vararg parts: String): JsonMap {
    return field(
        FieldExpression.create(*parts)
    )
}

fun createCriteriaToTextConverter(): CriteriaToTextConverter {

    val opMap = tai.criteria.operators.OperationMapImpl(
        OPERATION_MAP.operationMap + mapOf(
            field_ to FieldExpOperator()
        )
    )

    return CriteriaToTextConverterImpl(
        operationMap = opMap,
        criteriaDialectBuilder = CriteriaDialectBuilderImpl()
    )
}