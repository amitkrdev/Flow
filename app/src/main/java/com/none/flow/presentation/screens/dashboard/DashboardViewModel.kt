package com.none.flow.presentation.screens.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.none.flow.data.model.UserHabit
import com.none.flow.data.repository.HabitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val habitsRepository: HabitsRepository,
) : ViewModel() {

    var isUndoArchiveOptionVisible by mutableStateOf(false)
    private var lastArchivedHabitId: String? = null

    val feedUiState: StateFlow<HabitsFeedUiState> =
        habitsRepository.getAllUserHabitsFlow()
            .map { habits ->
                if (habits.isEmpty()) HabitsFeedUiState.Empty else HabitsFeedUiState.Success(habits)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HabitsFeedUiState.Loading,
            )

    fun

    fun archiveHabit(habitId: String) {
        viewModelScope.launch {
            isUndoArchiveOptionVisible = true
            lastArchivedHabitId = habitId
            habitsRepository.archiveHabit(habitId)
        }
    }

    fun undoLastArchivedHabit() {
        viewModelScope.launch {
            lastArchivedHabitId?.let {
                habitsRepository.restoreHabit(it)
            }
        }
        resetUndoArchiveState()
    }

    fun resetUndoArchiveState() {
        isUndoArchiveOptionVisible = false
        lastArchivedHabitId = null
    }
}

sealed interface HabitsFeedUiState {
    data object Loading : HabitsFeedUiState

    data class Success(
        val userHabits: List<UserHabit>,
    ) : HabitsFeedUiState

    data object Empty : HabitsFeedUiState

}
