import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverterImpl
import tai.criteria.operators.OPERATION_MAP
import tai.criteria.ops.*
import kotlin.streams.asSequence
import kotlin.streams.asStream

fun main() {
    val converterImpl = CriteriaToTextConverterImpl(OPERATION_MAP, CriteriaDialectBuilderImpl())
    val kk = converterImpl.convert(
        asOp(
            joinExpressions(
                listOf(
                    select(column("username")),
                    from(table("users"))
                ),
                isParenthesis = true
            ),
            "k"
        )
    )
    println(kk)
}