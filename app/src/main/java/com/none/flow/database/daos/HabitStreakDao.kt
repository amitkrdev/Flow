package com.none.flow.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.none.flow.database.entities.HabitStreakEntity

@Dao
interface HabitStreakDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStreak(streak: HabitStreakEntity): Long

    @Update
    suspend fun updateStreak(streak: HabitStreakEntity)
}
