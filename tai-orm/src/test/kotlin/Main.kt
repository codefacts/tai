
interface Kala {

}

fun main() {
    val it: Iterable<Int> = listOf(1)
    it.toList().asSequence().toList()
}