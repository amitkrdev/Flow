package com.none.flow.presentation.screens.archived

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.none.flow.data.model.Habit
import com.none.flow.data.repository.HabitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val habitsRepository: HabitsRepository,
) : ViewModel() {

    val archivedHabitsUiState: StateFlow<ArchivedHabitsUiState> =
        habitsRepository.getArchivedHabitsFlow()
            .map { habits ->
                if (habits.isEmpty()) ArchivedHabitsUiState.Empty else ArchivedHabitsUiState.Success(habits)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ArchivedHabitsUiState.Loading,
            )

    private val _selectedHabitIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedHabitIds: StateFlow<Set<String>> = _selectedHabitIds.asStateFlow()

    fun selectHabit(habitId: String) {
        _selectedHabitIds.value = _selectedHabitIds.value.plus(habitId)
    }

    fun deselectHabit(habitId: String) {
        _selectedHabitIds.value = _selectedHabitIds.value.minus(habitId)
    }

    fun clearAllSelections() {
        _selectedHabitIds.value = emptySet()
    }

    fun removeAllSelectedHabits() {
        viewModelScope.launch {
            val selectedHabits = _selectedHabitIds.value.toList()
            habitsRepository.deleteHabits(selectedHabits)
            clearAllSelections()
        }
    }

    fun restoreAllSelectedHabits() {
        viewModelScope.launch {
            val selectedHabits = _selectedHabitIds.value.toList()
            habitsRepository.restoreHabits(selectedHabits)
            clearAllSelections()
        }
    }

    fun removeHabitById(habitId: String) {
        viewModelScope.launch {
            habitsRepository.deleteHabit(habitId)
        }
    }

    fun restoreHabitById(habitId: String) {
        viewModelScope.launch {
            habitsRepository.restoreHabit(habitId)
        }
    }
}

sealed interface ArchivedHabitsUiState {
    data object Loading : ArchivedHabitsUiState

    data class Success(
        val habits: List<Habit>,
    ) : ArchivedHabitsUiState

    data object Empty : ArchivedHabitsUiState
}
