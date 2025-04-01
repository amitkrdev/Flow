package com.none.flow.data.repository

import com.none.flow.data.model.UserPreferences
import com.none.flow.data.model.enums.ThemeMode
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userDisplayNameFlow: Flow<String>
    val userPreferencesFlow: Flow<UserPreferences>
    val onboardingStatusFlow: Flow<Boolean>

    suspend fun updateDisplayName(name: String)
    suspend fun updateAppThemeMode(theme: ThemeMode)
    suspend fun updateDynamicColorEnabled(enabled: Boolean)
    suspend fun updateOnboardingCompleted(completed: Boolean)
}
