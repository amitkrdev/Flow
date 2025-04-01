package com.none.flow.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.none.flow.data.model.UserHabit
import com.none.flow.database.entities.HabitEntity
import com.none.flow.database.entities.HabitLogEntity
import com.none.flow.database.entities.HabitScheduleEntity
import com.none.flow.database.entities.HabitStreakEntity
import com.none.flow.database.entities.asExternalModel

data class PopulatedHabit(
    @Embedded val habit: HabitEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val schedule: HabitScheduleEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val streak: HabitStreakEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val logs: List<HabitLogEntity>
)

fun PopulatedHabit.asExternalModel(): UserHabit =
    UserHabit(
        habit = habit.asExternalModel(),
        schedule = schedule.asExternalModel(),
        streak = streak.asExternalModel(),
        logs = logs.map(HabitLogEntity::asExternalModel)
    )