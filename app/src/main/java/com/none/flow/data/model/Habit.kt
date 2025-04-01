package com.none.flow.data.model

import com.none.flow.database.entities.HabitEntity
import java.time.Instant

data class Habit(
    val id: String,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val themeColor: Int,
    val orderIndex: Int = 0,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val isActive: Boolean = true,
)

fun Habit.asEntity(): HabitEntity =
    HabitEntity(
        id = id,
        name = name,
        description = description,
        iconEmoji = iconEmoji,
        themeColor = themeColor,
        orderIndex = orderIndex,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isActive = isActive,
    )

