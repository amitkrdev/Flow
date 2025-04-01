package com.none.flow.data.repository.module

import com.none.flow.data.repository.HabitsRepository
import com.none.flow.data.repository.UserAccountRepository
import com.none.flow.data.repository.UserPreferencesRepository
import com.none.flow.data.repository.impl.HabitsRepositoryImpl
import com.none.flow.data.repository.impl.UserAccountRepositoryImpl
import com.none.flow.data.repository.impl.UserPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsHabitsRepository(
        habitsRepository: HabitsRepositoryImpl
    ): HabitsRepository

    @Binds
    abstract fun bindsUserAccountRepository(
        userAccountRepository: UserAccountRepositoryImpl
    ): UserAccountRepository

    @Binds
    abstract fun bindsUserPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}
