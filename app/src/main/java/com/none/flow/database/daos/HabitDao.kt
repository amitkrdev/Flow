package com.none.flow.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.none.flow.data.model.HabitStreak
import com.none.flow.database.entities.HabitEntity
import com.none.flow.database.entities.HabitLogEntity
import com.none.flow.database.entities.HabitScheduleEntity
import com.none.flow.database.entities.HabitStreakEntity
import com.none.flow.database.relationships.PopulatedHabit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Transaction
    @Query("SELECT * FROM habit_entries WHERE is_active = 1 ORDER BY order_index ASC")
    fun getAllUserHabitsStream(): Flow<List<PopulatedHabit>>

    @Query("SELECT * FROM habit_entries WHERE is_active = 1 ORDER BY order_index ASC")
    fun getActiveHabitsStream(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habit_entries WHERE is_active = 0 ORDER BY order_index ASC")
    fun getArchivedHabitsStream(): Flow<List<HabitEntity>>

    @Transaction
    @Query("SELECT * FROM habit_entries WHERE id = :habitId")
    fun getUserHabitStreamById(habitId: String): Flow<PopulatedHabit>

    @Transaction
    @Query("SELECT * FROM habit_entries WHERE id = :habitId")
    suspend fun fetchUserHabitById(habitId: String): PopulatedHabit?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("UPDATE habit_entries SET order_index = :order WHERE id = :habitId")
    suspend fun updateHabitOrder(habitId: String, order: Int)

    @Query("UPDATE habit_entries SET is_active = 0 WHERE id = :habitId")
    suspend fun archiveHabitById(habitId: String)

    @Query("UPDATE habit_entries SET is_active = 1 WHERE id = :habitId")
    suspend fun restoreHabitById(habitId: String)

    @Query("UPDATE habit_entries SET is_active = 1 WHERE id in (:habitIds)")
    suspend fun restoreHabits(habitIds: List<String>)

    @Query("DELETE FROM habit_entries WHERE id = :habitId")
    suspend fun removeHabitById(habitId: String)

    @Query("DELETE FROM habit_entries WHERE id in (:habitIds)")
    suspend fun removeHabits(habitIds: List<String>)

    @Query("DELETE FROM habit_entries")
    suspend fun removeAllHabits()
}