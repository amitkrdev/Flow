package com.none.flow.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.none.flow.data.model.HabitStreak
import java.time.LocalDate

@Entity(
    tableName = "habit_streaks_entries",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["habit_id"], unique = true)]
)
data class HabitStreakEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "habit_id")
    val habitId: String,
    @ColumnInfo(name = "active_streak")
    val activeStreakCount: Int,
    @ColumnInfo(name = "max_streak")
    val maxStreakCount: Int,
    @ColumnInfo(name = "completion_count")
    val completionCount: Int,
    @ColumnInfo(name = "last_completion_date")
    val lastCompletionDate: LocalDate,
    @ColumnInfo(name = "last_skipped_date")
    val lastSkippedDate: LocalDate?
)

fun HabitStreakEntity.asExternalModel(): HabitStreak =
    HabitStreak(
        id = id,
        habitId = habitId,
        activeStreakCount = activeStreakCount,
        maxStreakCount = maxStreakCount,
        completionCount = completionCount,
        lastCompletionDate = lastCompletionDate,
        lastSkippedDate = lastSkippedDate
    )