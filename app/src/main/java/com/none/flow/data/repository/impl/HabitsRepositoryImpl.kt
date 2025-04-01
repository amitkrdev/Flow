package com.none.flow.data.repository.impl

import com.none.flow.data.model.Habit
import com.none.flow.data.model.HabitLog
import com.none.flow.data.model.HabitSchedule
import com.none.flow.data.model.HabitStreak
import com.none.flow.data.model.User
import com.none.flow.data.model.UserHabit
import com.none.flow.data.model.asEntity
import com.none.flow.data.repository.HabitsRepository
import com.none.flow.database.daos.HabitDao
import com.none.flow.database.daos.HabitLogDao
import com.none.flow.database.daos.HabitScheduleDao
import com.none.flow.database.daos.HabitStreakDao
import com.none.flow.database.entities.HabitEntity
import com.none.flow.database.entities.HabitStreakEntity
import com.none.flow.database.entities.asExternalModel
import com.none.flow.database.relationships.PopulatedHabit
import com.none.flow.database.relationships.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class HabitsRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val scheduleDao: HabitScheduleDao,
    private val streakDao: HabitStreakDao,
    private val logDao: HabitLogDao
) : HabitsRepository {

    override fun getAllUserHabitsFlow(): Flow<List<UserHabit>> =
        habitDao.getAllUserHabitsStream().map { it.map(PopulatedHabit::asExternalModel) }

    override fun getActiveHabitsFlow(): Flow<List<Habit>> =
        habitDao.getActiveHabitsStream().map { it.map(HabitEntity::asExternalModel) }

    override fun getArchivedHabitsFlow(): Flow<List<Habit>> =
        habitDao.getArchivedHabitsStream().map { it.map(HabitEntity::asExternalModel) }

    override fun getHabitFlowById(habitId: String): Flow<UserHabit?> =
        habitDao.getUserHabitStreamById(habitId).mapNotNull { it.asExternalModel() }

    override suspend fun fetchHabitById(habitId: String): UserHabit? =
        habitDao.fetchUserHabitById(habitId)?.asExternalModel()

    override suspend fun createHabit(habit: Habit, schedule: HabitSchedule) {
        habitDao.insertHabit(habit.asEntity())
        scheduleDao.insertSchedule(schedule.asEntity())
        streakDao.insertStreak(
            HabitStreak(habitId = habit.id).asEntity()
        )
    }

    override suspend fun updateHabit(habit: Habit, schedule: HabitSchedule) {
        habitDao.updateHabit(habit.asEntity())
        scheduleDao.updateSchedule(schedule.asEntity())
    }

    override suspend fun updateHabitSortOrder(habitId: String, sortOrder: Int) {
        habitDao.updateHabitOrder(habitId, sortOrder)
    }

    override suspend fun completeHabit(habitId: String) {
        val log = HabitLog(habitId = habitId)
        logDao.insertLog(log.asEntity())
    }

    override suspend fun archiveHabit(habitId: String) {
        habitDao.archiveHabitById(habitId)
    }

    override suspend fun restoreHabit(habitId: String) {
        habitDao.restoreHabitById(habitId)
    }

    override suspend fun restoreHabits(habitIds: List<String>) {
        habitDao.restoreHabits(habitIds)
    }

    override suspend fun deleteHabit(habitId: String) {
        habitDao.removeHabitById(habitId)
    }

    override suspend fun deleteHabits(habitIds: List<String>) {
        habitDao.removeHabits(habitIds)
    }
}
