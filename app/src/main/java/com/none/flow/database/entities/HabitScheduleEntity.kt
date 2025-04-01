package com.none.flow.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.none.flow.data.model.HabitSchedule
import com.none.flow.data.model.enums.ScheduleFrequency
import java.time.DayOfWeek
import java.time.Instant

@Entity(
    tableName = "habit_schedule_entries",
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
data class HabitScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "habit_id")
    val habitId: String,
    @ColumnInfo(name = "recurrence_days")
    val recurrenceDays: Set<DayOfWeek>,
    @ColumnInfo(name = "repeat_interval")
    val repeatInterval: ScheduleFrequency,
    @ColumnInfo(name = "interval_count")
    val repeatIntervalCount: Int,
    @ColumnInfo(name = "daily_goal")
    val dailyGoal: Int,
    @ColumnInfo(name = "start_date")
    val startDate: Instant,
    @ColumnInfo(name = "end_date")
    val endDate: Instant?
)

fun HabitScheduleEntity.asExternalModel(): HabitSchedule =
    HabitSchedule(
        id = id,
        habitId = habitId,
        recurrenceDays = recurrenceDays,
        repeatInterval = repeatInterval,
        repeatIntervalCount = repeatIntervalCount,
        dailyGoal = dailyGoal,
        startDate = startDate,
        endDate = endDate
    )