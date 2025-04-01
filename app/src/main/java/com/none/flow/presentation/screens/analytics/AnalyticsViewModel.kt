package com.none.flow.presentation.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.none.flow.data.model.Habit
import com.none.flow.data.model.UserHabit
import com.none.flow.data.repository.HabitsRepository
import com.none.flow.presentation.screens.dashboard.HabitsFeedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: HabitsRepository,
) : ViewModel() {

//    val feedUiState: StateFlow<HabitsFeedUiState> =
//        repository.getHabitsStream()
//            .map(HabitsFeedUiState::Success)
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                initialValue = HabitsFeedUiState.Loading,
//            )

}

sealed interface AnalyticsUiState {
    data object Loading : AnalyticsUiState
    data object Error : AnalyticsUiState
    data class Success(val habit: UserHabit) : AnalyticsUiState
}