import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverterImpl
import tai.criteria.operators.OPERATION_MAP
import tai.criteria.ops.*
import java.io.File
import java.nio.file.Files
import kotlin.streams.asSequence
import kotlin.streams.asStream

fun main() {
    println(
        """
            facebook:
              verify: "<verify>"
              secret: "<your secret>"
              page-access-token: "<your page access token>"
        """.trimIndent()
    )
}

fun testOp() {
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
