import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverterImpl
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
            where = and(
                eq(
                    column("a", "name"), valueOf("name1")
                ),
                gt(
                    column("a", "age"), valueOf(18)
                )
            ),
            groupBy = listOf(
                column("a", "name"),
                column("a", "age"),
                column("a", "id")
            ),
            having = and(
                gt(
                    avg(column("a", "price")), valueOf(500)
                ),
                eq(
                    column("a", "earned"), valueOf(12000)
                )
            )
        )
    );
    println(sql)
}