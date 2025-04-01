package com.none.flow.data.model

import java.time.Instant

data class UserHabit internal constructor(
    val id: String,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val themeColor: Int,
    val orderIndex: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isActive: Boolean,
    val schedule: HabitSchedule,
    val streak: HabitStreak,
    val logs: List<HabitLog>,
) {
    constructor(
        habit: Habit,
        schedule: HabitSchedule,
        streak: HabitStreak,
        logs: List<HabitLog>
    ) : this(
        id = habit.id,
        name = habit.name,
        description = habit.description,
        iconEmoji = habit.iconEmoji,
        themeColor = habit.themeColor,
        orderIndex = habit.orderIndex,
        createdAt = habit.createdAt,
        updatedAt = habit.updatedAt,
        isActive = habit.isActive,
        schedule = schedule,
        streak = streak,
        logs = logs
    )
}
