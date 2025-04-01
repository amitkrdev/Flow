package com.none.flow.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.none.flow.data.model.HabitLog
import com.none.flow.database.daos.HabitDao
import com.none.flow.database.daos.HabitLogDao
import com.none.flow.database.daos.HabitScheduleDao
import com.none.flow.database.daos.HabitStreakDao
import com.none.flow.database.entities.HabitEntity
import com.none.flow.database.entities.HabitLogEntity
import com.none.flow.database.entities.HabitScheduleEntity
import com.none.flow.database.entities.HabitStreakEntity
import com.none.flow.database.utils.DayOfWeekSetConverter
import com.none.flow.database.utils.InstantConverter
import com.none.flow.database.utils.LocalDateConverter

@Database(
    entities = [
        HabitEntity::class,
        HabitLogEntity::class,
        HabitStreakEntity::class,
        HabitScheduleEntity::class,
    ],
    version = 1,
    exportSchema = false // TODO: Enable schema export for version tracking in production
)
@TypeConverters(
    InstantConverter::class,
    LocalDateConverter::class,
    DayOfWeekSetConverter::class
)
abstract class FlowDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao
    abstract fun habitScheduleDao(): HabitScheduleDao
    abstract fun habitStreakDao(): HabitStreakDao
}