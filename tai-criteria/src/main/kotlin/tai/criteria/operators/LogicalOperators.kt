package tai.criteria.operators

import tai.base.JsonMap
import tai.criteria.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class AndOperatorImpl : CriteriaOperation1 {

    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(
            arg_, true, null, ::combineMulti
        )
    )

    private fun combineMulti(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        val expBuilder = CriteriaExpressionBuilderImpl();
        if (list.isEmpty()) {
            return criteriaDialect.toExpression(true);
        }
        expBuilder.add("( ");
        for (i in 0..(list.size - 2)) {
            expBuilder.add(list[i]).add(" AND ");
        }
        return expBuilder.add(list[list.size - 1]).add(" )").build();
    }

    override fun renderExpression(
        dialect: CriteriaDialect,
        param: CriteriaExpression
    ): CriteriaExpression {
        return param;
    }
}

fun main() {
    val kk = CriteriaToTextConverterImpl(
        operationMap,
        criteriaDialectBuilder = CriteriaDialectBuilderImpl()
    );
    val exp = kk.convert(
        and(
            and(
                valueOf(Date()),
                valueOf("Amari"),
                valueOf(true),
                valueOf(12)
            ),
            and(
                valueOf(Date()),
                valueOf("Amari"),
                valueOf(true),
                valueOf(12)
            )
        )
    )
    println(exp)
}