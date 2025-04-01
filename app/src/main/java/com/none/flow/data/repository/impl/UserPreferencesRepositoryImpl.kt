package com.none.flow.data.repository.impl

import com.none.flow.data.model.UserPreferences
import com.none.flow.data.model.enums.ThemeMode
import com.none.flow.data.repository.UserPreferencesRepository
import com.none.flow.data.service.FlowPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val preferencesStorage: FlowPreferencesDataSource
) : UserPreferencesRepository {

    override val userDisplayNameFlow: Flow<String>
        get() = preferencesStorage.userDisplayNameFlow

    override val onboardingStatusFlow: Flow<Boolean>
        get() = preferencesStorage.onboardingStatusFlow

    override val userPreferencesFlow: Flow<UserPreferences>
        get() = preferencesStorage.userPreferencesFlow

    override suspend fun updateDisplayName(name: String) {
        preferencesStorage.setDisplayName(name)
    }

    override suspend fun updateAppThemeMode(theme: ThemeMode) {
        preferencesStorage.setAppThemeMode(theme)
    }

    override suspend fun updateDynamicColorEnabled(enabled: Boolean) {
        preferencesStorage.setDynamicColorEnabled(enabled)
    }

    override suspend fun updateOnboardingCompleted(completed: Boolean) {
        preferencesStorage.setOnboardingCompleted(completed)
    }
}
