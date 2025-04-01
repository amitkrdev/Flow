package com.none.flow.database.utils

import androidx.room.TypeConverter
import java.time.DayOfWeek

internal class DayOfWeekSetConverter {

    @TypeConverter
    fun fromSet(days: Set<DayOfWeek>?): String {
        return days?.joinToString(",") { it.name } ?: ""
    }

    @TypeConverter
    fun toSet(daysString: String?): Set<DayOfWeek> {
        return daysString?.takeUnless { it.isBlank() }
            ?.split(",")
            ?.map { DayOfWeek.valueOf(it) }
            ?.toSet() ?: emptySet()
    }
}
