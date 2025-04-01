package com.none.flow.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.none.flow.database.entities.HabitLogEntity

@Dao
interface HabitLogDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLog(log: HabitLogEntity): Long

    @Update
    suspend fun updateLog(log: HabitLogEntity)
}
