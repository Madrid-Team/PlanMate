package domain.utlis

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.convertDateIntoReadableDate(): String {

    val parsedDateTime = LocalDateTime.parse(this.toString())
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm a")
    val formattedDateTime = parsedDateTime.format(outputFormatter)

    return formattedDateTime
}
