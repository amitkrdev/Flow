package com.none.flow.data.model

import com.none.flow.database.entities.HabitStreakEntity
import java.time.LocalDate

data class HabitStreak(
    val id: Long = 0,
    val habitId: String,
    val activeStreakCount: Int = 0,
    val maxStreakCount: Int = 0,
    val completionCount: Int = 0,
    val lastCompletionDate: LocalDate = LocalDate.now(),
    val lastSkippedDate: LocalDate? = null
)

fun HabitStreak.asEntity(): HabitStreakEntity =
    HabitStreakEntity(
        id = id,
        habitId = habitId,
        activeStreakCount = activeStreakCount,
        maxStreakCount = maxStreakCount,
        completionCount = completionCount,
        lastCompletionDate = lastCompletionDate,
        lastSkippedDate = lastSkippedDate
    )
