package com.none.flow.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.none.flow.data.model.HabitLog
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "habit_log_entries",
    foreignKeys = [ForeignKey(
        entity = HabitEntity::class,
        parentColumns = ["id"],
        childColumns = ["habit_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["habit_id"]),
        Index(value = ["completion_date"])
    ]
)
data class HabitLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "habit_id")
    val habitId: String,
    @ColumnInfo(name = "completion_date")
    val completionDate: LocalDate,
    @ColumnInfo(name = "timezone_offset")
    val timezoneOffset: Int,
    @ColumnInfo(name = "completion_amount")
    val completionAmount: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
)

fun HabitLogEntity.asExternalModel(): HabitLog =
    HabitLog(
        id = id,
        habitId = habitId,
        completionDate = completionDate,
        timezoneOffset = timezoneOffset,
        completionAmount = completionAmount,
        createdAt = createdAt
    )