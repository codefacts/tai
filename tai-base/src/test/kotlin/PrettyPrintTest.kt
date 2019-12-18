import org.junit.Test
import tai.base.TextAndCollection
import tai.base.prettyPrint

class PrettyPrintTest {
    @Test
    fun test() {
        val pr = prettyPrint(listOf(
            2, 3, 4, listOf(
                1, 4, 5, 5,
                mapOf(22 to 45, 23 to 47),
                listOf(84, 89, 92)
            ),
            listOf(3, 2, 5)
        )
        ) { if (it is Number) TextAndCollection(it.toString(), null) else TextAndCollection("", it) }
        println("result: " + pr)
    }
}