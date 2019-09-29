import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverterImpl
import tai.criteria.operators.operationMap
import tai.criteria.ops.*

fun main() {
    val kk = CriteriaToTextConverterImpl(operationMap, CriteriaDialectBuilderImpl());
    val sql = kk.convert(
        sqlQuery(
            select = listOf(
                column("u", "username"),
                column("u", "password"),
                column("address")
            ),
            from = listOf(
                join(
                    asOp(
                        table("mmc", "user"), "u", true
                    ),
                    asOp(
                        table("mmc", "person"), "p", true
                    ),
                    eq(
                        column("u", "person_id"),
                        column("p", "id")
                    )
                ),
                join(
                    asOp(
                        table("mmc", "user"), "u", true
                    ),
                    asOp(
                        table("mmc", "person"), "p", true
                    ),
                    eq(
                        column("u", "person_id"),
                        column("p", "id")
                    )
                )
            )
        )
    );
    println(sql)
}