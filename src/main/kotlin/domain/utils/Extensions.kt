package domain.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


@FormatStringsInDatetimeFormats
fun LocalDateTime.convertDateToReadableDate(): String {
    val isDstMonth = this.monthNumber in 4..10
    // Add +1 hour if in DST months
    val adjustedDateTime = if (isDstMonth) {
        this.toInstant(TimeZone.of("Africa/Cairo")).plus(1, DateTimeUnit.HOUR).toLocalDateTime(TimeZone.of("Africa/Cairo"))
    } else {
        this
    }

    val outputFormatter: DateTimeFormat<LocalDateTime> = LocalDateTime.Format {
        byUnicodePattern("yyyy/MM/dd HH:mm")
    }
    return outputFormatter.format(adjustedDateTime)
}







