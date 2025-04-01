package com.none.flow.database.module

import android.content.Context
import androidx.room.Room
import com.none.flow.database.FlowDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    private const val DATABASE_NAME = "flow-database"

    @Provides
    @Singleton
    fun providesFlowDatabase(
        @ApplicationContext context: Context,
    ): FlowDatabase = Room.databaseBuilder(
        context,
        FlowDatabase::class.java,
        DATABASE_NAME,
    )
        // TODO: Remove fallbackToDestructiveMigration() in production version
        .fallbackToDestructiveMigration()
        .build()
}
