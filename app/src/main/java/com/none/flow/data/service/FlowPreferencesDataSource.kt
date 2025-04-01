package com.none.flow.data.service

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.none.flow.data.model.UserPreferences
import com.none.flow.data.model.enums.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class FlowPreferencesDataSource @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val KEY_DISPLAY_NAME = stringPreferencesKey("display_name")
        val KEY_APP_THEME_MODE = stringPreferencesKey("theme_mode")
        val KEY_DYNAMIC_COLOR_ENABLED = booleanPreferencesKey("enable_dynamic_color")
        val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    private val Preferences.displayName: String
        get() = this[PreferencesKeys.KEY_DISPLAY_NAME] ?: "Guest"

    private val Preferences.isOnboardingCompleted: Boolean
        get() = this[PreferencesKeys.KEY_ONBOARDING_COMPLETED] ?: false

    private val Preferences.appThemeMode: ThemeMode
        get() = this[PreferencesKeys.KEY_APP_THEME_MODE]?.let(ThemeMode::valueOf) ?: ThemeMode.SYSTEM

    private val Preferences.isDynamicColorEnabled: Boolean
        get() = this[PreferencesKeys.KEY_DYNAMIC_COLOR_ENABLED] ?: false

    val userDisplayNameFlow: Flow<String> = preferencesDataStore.data
        .map { it.displayName }
        .distinctUntilChanged()

    val onboardingStatusFlow: Flow<Boolean> = preferencesDataStore.data
        .map { it.isOnboardingCompleted }
        .distinctUntilChanged()

    val userPreferencesFlow: Flow<UserPreferences> = preferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPreferences(
                theme = preferences.appThemeMode,
                enableDynamicColor = preferences.isDynamicColorEnabled
            )
        }
        .distinctUntilChanged()

    suspend fun setDisplayName(name: String) {
        try {
            preferencesDataStore.edit { it[PreferencesKeys.KEY_DISPLAY_NAME] = name }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error updating display name: ${e.message}", e)
        }
    }

    suspend fun setAppThemeMode(theme: ThemeMode) {
        try {
            preferencesDataStore.edit { it[PreferencesKeys.KEY_APP_THEME_MODE] = theme.name }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error updating theme mode: ${e.message}", e)
        }
    }

    suspend fun setDynamicColorEnabled(enabled: Boolean) {
        try {
            preferencesDataStore.edit { it[PreferencesKeys.KEY_DYNAMIC_COLOR_ENABLED] = enabled }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error updating dynamic color preference: ${e.message}", e)
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        try {
            preferencesDataStore.edit { it[PreferencesKeys.KEY_ONBOARDING_COMPLETED] = completed }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error updating onboarding status: ${e.message}", e)
        }
    }

    companion object {
        private const val LOG_TAG = "UserPreferencesStorage"
    }
}
