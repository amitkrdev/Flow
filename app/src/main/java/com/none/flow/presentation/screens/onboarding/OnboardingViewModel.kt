package com.none.flow.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.none.flow.data.repository.UserAccountRepository
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
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userAccountRepository: UserAccountRepository
) : ViewModel() {

    val onboardingUiState: StateFlow<OnboardingUiState> =
        userPreferencesRepository.onboardingStatusFlow
            .map { isCompleted ->
                if (isCompleted) OnboardingUiState.Complete
                else OnboardingUiState.Incomplete
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = OnboardingUiState.Incomplete,
            )

    fun markOnboardingAsComplete() {
        viewModelScope.launch {
            userPreferencesRepository.updateOnboardingCompleted(true)
            if (userAccountRepository.isUserAuthenticated().not()) {
                createAnonymousUser()
            }
        }
    }

    private fun createAnonymousUser() {
        viewModelScope.launch {
            userAccountRepository.signInAnonymously()
        }
    }
}


sealed interface OnboardingUiState {
    data object Incomplete : OnboardingUiState
    data object Complete : OnboardingUiState
}
