import org.junit.Assert
import org.junit.Test
import tai.orm.JoinParam
import tai.orm.core.PathExpression
import tai.orm.query.impl.createAliasToFullPathExpMap

class FullPathExpTest {

    @Test
    fun test() {
        val listOf = listOf(
            JoinParam(PathExpression.parse("b.k5.k6"), "c"),
            JoinParam(PathExpression.parse("a.k3.k4"), "b"),
            JoinParam(PathExpression.parse("r.k1.k2"), "a")
        )
        val map = createAliasToFullPathExpMap(
            "r",
            listOf,
            listOf.asSequence().map { it.alias to it }.toMap()
        )
        println("result: " + map)
        Assert.assertEquals(map, mapOf(
            "a" to PathExpression.parse("r.k1.k2"),
            "b" to PathExpression.parse("r.k1.k2.k3.k4"),
            "c" to PathExpression.parse("r.k1.k2.k3.k4.k5.k6")
        ))
    }
}