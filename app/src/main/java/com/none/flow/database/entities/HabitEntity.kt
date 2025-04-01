package com.none.flow.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.none.flow.data.model.Habit
import com.none.flow.data.model.HabitSchedule
import java.time.Instant

@Entity(tableName = "habit_entries")
data class HabitEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val themeColor: Int,
    @ColumnInfo(name = "order_index")
    val orderIndex: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Instant,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean
)

fun HabitEntity.asExternalModel(): Habit =
    Habit(
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
