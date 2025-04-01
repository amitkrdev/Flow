package com.none.flow

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.none.flow.data.model.UserPreferences
import com.none.flow.data.model.enums.ThemeMode
import com.none.flow.data.repository.UserPreferencesRepository
import com.none.flow.presentation.screens.dashboard.navigation.DashboardRoute
import com.none.flow.presentation.screens.onboarding.navigation.OnboardingRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
class FlowMainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> =
        userPreferencesRepository.userPreferencesFlow
            .map(MainActivityUiState::Success)
            .stateIn(
                scope = viewModelScope,
                initialValue = MainActivityUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000),
            )

    private val _startDestination = mutableStateOf<KClass<out Any>?>(null)
    val startDestination: State<KClass<out Any>?> = _startDestination

    fun determineStartDestination() {
        viewModelScope.launch {
            val hasCompletedOnboarding = userPreferencesRepository.onboardingStatusFlow.first()
            _startDestination.value =
                if (hasCompletedOnboarding) DashboardRoute::class else OnboardingRoute::class
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userPreferences: UserPreferences) : MainActivityUiState {

        override val isDynamicThemingEnabled: Boolean
            get() = userPreferences.enableDynamicColor

        override fun isDarkThemeEnabled(isSystemDarkTheme: Boolean): Boolean =
            when (userPreferences.theme) {
                ThemeMode.DAY -> false
                ThemeMode.NIGHT -> true
                ThemeMode.SYSTEM -> isSystemDarkTheme
            }
    }

    fun shouldDisplaySplashScreen(): Boolean = this is Loading

    fun isDarkThemeEnabled(isSystemDarkTheme: Boolean): Boolean = isSystemDarkTheme

    val isDynamicThemingEnabled: Boolean
        get() = true
}