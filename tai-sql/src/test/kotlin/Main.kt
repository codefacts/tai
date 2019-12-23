import kotlin.streams.asSequence
import kotlin.streams.asStream

fun main() {
    val it: Iterable<String> = listOf<String>()
    val stream = listOf<String>().stream()
    stream.asSequence().asStream().asSequence()
    it.asSequence().asSequence()
}