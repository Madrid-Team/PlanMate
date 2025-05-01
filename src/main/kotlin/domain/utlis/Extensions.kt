package domain.utlis

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.convertDateIntoReadableDate(): String {

    val parsedDateTime = LocalDateTime.parse(this.toString())
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm a")
    val formattedDateTime = parsedDateTime.format(outputFormatter)

    return formattedDateTime
}

fun String.isValidString(): Boolean {
    val regex = Regex("^[a-zA-Z0-9_-]{3,30}$")
    return this.matches(regex)
}