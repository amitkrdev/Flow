package com.none.flow.data.model

import com.none.flow.data.model.enums.ScheduleFrequency
import com.none.flow.database.entities.HabitScheduleEntity
import java.time.DayOfWeek
import java.time.Instant

data class HabitSchedule(
    val id: Long = 0,
    val habitId: String,
    val recurrenceDays: Set<DayOfWeek>,
    val repeatInterval: ScheduleFrequency,
    val repeatIntervalCount: Int,
    val dailyGoal: Int,
    val startDate: Instant = Instant.now(),
    val endDate: Instant? = null
)

fun HabitSchedule.asEntity(): HabitScheduleEntity =
    HabitScheduleEntity(
        id = id,
        habitId = habitId,
        recurrenceDays = recurrenceDays,
        repeatInterval = repeatInterval,
        repeatIntervalCount = repeatIntervalCount,
        dailyGoal = dailyGoal,
        startDate = startDate,
        endDate = endDate
    )