package com.none.flow.utils

import java.text.DateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

fun LocalTime.toReminderTime(): String {
    val formatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, this@toReminderTime.hour)
        set(Calendar.MINUTE, this@toReminderTime.minute)
        set(Calendar.SECOND, 0)
    }
    val date = calendar.time

    return formatter.format(date).uppercase()
}
