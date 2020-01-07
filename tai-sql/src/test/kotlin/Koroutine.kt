import java.util.regex.Pattern

fun main() {

    val currentTimeMillis = System.currentTimeMillis()
    println(currentTimeMillis)
    println((currentTimeMillis + (Math.random() * 1000)).toInt())

    val (a, b) = longArrayOf(2, 4)
    println(a)
    println(b)

    val pattern = Pattern.compile("https?:\\/\\/[A-Za-z0-9][^\\s]*\\.[a-zA-Z0-9][^\\s\\(\\)\\{\\}\\[\\]\\\"\\']+")
}