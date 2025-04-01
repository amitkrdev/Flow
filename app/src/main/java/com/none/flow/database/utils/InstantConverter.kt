package com.none.flow.database.utils

import androidx.room.TypeConverter
import java.time.Instant

internal class InstantConverter {

    @TypeConverter
    fun fromInstant(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

    @TypeConverter
    fun toInstant(timestamp: Long?): Instant? {
        return timestamp?.let { Instant.ofEpochMilli(it) }
    }
}
