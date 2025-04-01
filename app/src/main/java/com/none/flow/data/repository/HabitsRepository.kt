package com.none.flow.data.repository

import com.none.flow.data.model.Habit
import com.none.flow.data.model.HabitLog
import com.none.flow.data.model.HabitSchedule
import com.none.flow.data.model.UserHabit
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

    fun getAllUserHabitsFlow(): Flow<List<UserHabit>>

    fun getActiveHabitsFlow(): Flow<List<Habit>>

    fun getArchivedHabitsFlow(): Flow<List<Habit>>

    fun getHabitFlowById(habitId: String): Flow<UserHabit?>

    suspend fun fetchHabitById(habitId: String): UserHabit?

    suspend fun createHabit(habit: Habit, schedule: HabitSchedule)

    suspend fun updateHabit(habit: Habit, schedule: HabitSchedule)

    suspend fun updateHabitSortOrder(habitId: String, sortOrder: Int)

    suspend fun completeHabit(habitId: String)

    suspend fun archiveHabit(habitId: String)

    suspend fun restoreHabit(habitId: String)

    suspend fun restoreHabits(habitIds: List<String>)

    suspend fun deleteHabit(habitId: String)

    suspend fun deleteHabits(habitIds: List<String>)
}