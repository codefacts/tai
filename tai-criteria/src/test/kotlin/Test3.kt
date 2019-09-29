import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverterImpl
import tai.criteria.operators.Order
import tai.criteria.operators.operationMap
import tai.criteria.ops.*

fun main() {
    val kk = CriteriaToTextConverterImpl(operationMap, CriteriaDialectBuilderImpl());
    val sql = kk.convert(
        sqlQuery(
            select = listOf(
                column("u", "username")
            ),
            from = listOf(
                asOp(
                    table("mmc", "person"), "p", true
                )
            ),
            orderBy = listOf(
                orderBy(
                    column("a", "name"), Order.ASC
                ),
                orderBy(
                    column("a", "name"), Order.DESC
                ),
                orderBy(
                    column("a", "name"), Order.ASC
                )
            )
        )
    );
    println(sql)
}