import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

fun main() {
    val dateTime = DateTimeFormatter.ISO_INSTANT.parse("2019-10-12T00:00:00.000Z");
    val date = Date(Instant.from(dateTime).toEpochMilli());
    val cal = GregorianCalendar();
    cal.time = date;
    println(
        cal.get(Calendar.MILLISECOND)
    )
}