package com.none.flow.data.model

import com.none.flow.database.entities.HabitLogEntity
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

data class HabitLog(
    val id: Long = 0,
    val habitId: String,
    val completionDate: LocalDate = LocalDate.now(),
    val timezoneOffset: Int = ZoneId.systemDefault().rules.getOffset(Instant.now()).totalSeconds / 60,
    val completionAmount: Int = 1,
    val createdAt: Instant = Instant.now()
)

fun HabitLog.asEntity(): HabitLogEntity =
    HabitLogEntity(
        id = id,
        habitId = habitId,
        completionDate = completionDate,
        timezoneOffset = timezoneOffset,
        completionAmount = completionAmount,
        createdAt = createdAt
    )