package com.park.conductor.common.utilities
import java.text.SimpleDateFormat
import java.util.*

fun getDate(today: Boolean = true, format: String): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    val formattedDate = dateFormat.format(calendar.time)
    return formattedDate
}

fun getFormattedDate(today: Boolean = true, format1: String?, format2: String, dateString: String = ""): String {
    return try {
        val locale = Locale.getDefault()
        val dateFormatOut = SimpleDateFormat(format2, locale)

        if (today) {
            val calendar = Calendar.getInstance()
            dateFormatOut.format(calendar.time)
        } else {
            val dateFormatIn = SimpleDateFormat(format1, locale)
            val parsedDate = dateFormatIn.parse(dateString)
            if (parsedDate != null) {
                dateFormatOut.format(parsedDate)
            } else {
                "Invalid date"
            }
        }
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}
