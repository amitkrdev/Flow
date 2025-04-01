package com.none.flow.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.none.flow.data.model.UserPreferences
import com.none.flow.data.model.enums.ThemeMode
import com.none.flow.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserPreferencesRepository
) : ViewModel() {

    val settingsUiState: StateFlow<SettingsUiState> =
        userSettingsRepository.userPreferencesFlow
            .map(SettingsUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = SettingsUiState.Loading,
            )

    fun onRestoreBackup() {}

    fun onCreateBackup() {}

    fun updateThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            userSettingsRepository.updateAppThemeMode(theme= themeMode)
        }
    }

    fun updateDynamicColorPreference(isEnabled: Boolean) {
        viewModelScope.launch {
            userSettingsRepository.updateDynamicColorEnabled(enabled = isEnabled)
        }
    }
}

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: UserPreferences) : SettingsUiState
}