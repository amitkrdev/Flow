package com.none.flow.database.module

import com.none.flow.database.FlowDatabase
import com.none.flow.database.daos.HabitDao
import com.none.flow.database.daos.HabitLogDao
import com.none.flow.database.daos.HabitScheduleDao
import com.none.flow.database.daos.HabitStreakDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun providesHabitDao(
        database: FlowDatabase,
    ): HabitDao = database.habitDao()


    @Provides
    fun providesHabitScheduleDao(
        database: FlowDatabase,
    ): HabitScheduleDao = database.habitScheduleDao()

    @Provides
    fun providesHabitStreakDao(
        database: FlowDatabase,
    ): HabitStreakDao = database.habitStreakDao()

    @Provides
    fun providesHabitLogDao(
        database: FlowDatabase,
    ): HabitLogDao = database.habitLogDao()

}
