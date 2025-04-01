package com.none.flow.presentation.screens.reorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.none.flow.data.model.Habit
import com.none.flow.data.repository.HabitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReorderHabitsViewModel @Inject constructor(
    private val habitsRepository: HabitsRepository
) : ViewModel() {
    val reorderHabitsUiState: StateFlow<ReorderHabitsUiState> =
        habitsRepository.getActiveHabitsFlow()
            .map { habits ->
                if (habits.isEmpty()) ReorderHabitsUiState.Empty else ReorderHabitsUiState.Success(
                    habits
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ReorderHabitsUiState.Loading,
            )

    fun moveHabit(habitId: String, targetIndex: Int) {
        viewModelScope.launch {
            habitsRepository.updateHabitSortOrder(habitId = habitId, sortOrder = targetIndex)
        }
//        _habitsList.update { currentList ->
//            val updatedList = currentList.toMutableList()
//            val movedItem = updatedList.removeAt(fromIndex)
//            updatedList.add(toIndex, movedItem)
//            updatedList
//        }
    }
}

sealed interface ReorderHabitsUiState {
    data object Loading : ReorderHabitsUiState

    data class Success(
        val habits: List<Habit>,
    ) : ReorderHabitsUiState

    data object Empty : ReorderHabitsUiState
}
