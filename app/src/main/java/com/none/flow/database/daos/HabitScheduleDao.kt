package com.none.flow.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.none.flow.database.entities.HabitScheduleEntity

@Dao
interface HabitScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: HabitScheduleEntity): Long

    @Update
    suspend fun updateSchedule(schedule: HabitScheduleEntity)
}
