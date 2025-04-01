package com.none.flow.presentation.screens.habiteditor

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.none.flow.R
import com.none.flow.data.model.Habit
import com.none.flow.data.model.HabitSchedule
import com.none.flow.data.model.enums.ScheduleFrequency
import com.none.flow.data.repository.HabitsRepository
import com.none.flow.presentation.screens.habiteditor.navigation.HabitEditorRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HabitEditorViewModel @Inject constructor(
    private val habitsRepository: HabitsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val selectedHabitId: String? = savedStateHandle.toRoute<HabitEditorRoute>().id

    private val _editorUiState = MutableStateFlow<HabitEditorUiState>(HabitEditorUiState.Loading)
    val editorUiState: StateFlow<HabitEditorUiState> = _editorUiState.asStateFlow()

    init {
        if (selectedHabitId != null) {
            loadHabit(selectedHabitId)
        } else {
            _editorUiState.value = HabitEditorUiState.Editing()
        }
    }

    fun saveHabit() {
        val editingState = editorUiState.value as? HabitEditorUiState.Editing ?: return

        editingState.validate()?.let { errorMessageResId ->
            updateUserMessage(errorMessageResId)
            return
        }

        val habitId = selectedHabitId ?: UUID.randomUUID().toString()

        val schedule = HabitSchedule(
            habitId = habitId,
            id = editingState.scheduleId,
            recurrenceDays = editingState.selectedDays,
            repeatInterval = editingState.interval,
            repeatIntervalCount = editingState.intervalCount!!,
            dailyGoal = editingState.dailyGoal!!,
        )

        val habit = Habit(
            id = habitId,
            name = editingState.name,
            description = editingState.description,
            iconEmoji = editingState.iconEmoji!!,
            themeColor = editingState.themeColor!!,
        )

        upsertHabit(habit = habit, schedule = schedule, isNew = selectedHabitId == null)
    }

    fun snackbarMessageShown() {
        updateUserMessage(messageResId = null)
    }

    fun updateName(newName: String) =
        updateEditingState { it.copy(name = newName) }

    fun updateDescription(newDescription: String) =
        updateEditingState { it.copy(description = newDescription) }

    fun updateIconEmoji(newIconEmoji: String?) =
        updateEditingState { it.copy(iconEmoji = newIconEmoji) }

    fun updateThemeColor(newThemeColor: Int?) =
        updateEditingState { it.copy(themeColor = newThemeColor) }

    fun updateDailyGoal(newDailyGoal: Int?) =
        updateEditingState { it.copy(dailyGoal = newDailyGoal?.coerceIn(1, 9)) }

    fun updateInterval(newInterval: ScheduleFrequency) =
        updateEditingState { it.copy(interval = newInterval) }

    fun updateIntervalCount(newIntervalCount: Int?) =
        updateEditingState { it.copy(intervalCount = newIntervalCount?.coerceIn(1, 7)) }

    fun toggleSelectedDay(day: DayOfWeek) =
        updateEditingState { it.copy(selectedDays = it.selectedDays.toggle(day)) }

    fun addReminder(time: LocalTime) =
        updateReminders { it + (Random.nextInt(1, Int.MAX_VALUE) to time) }

    fun deleteReminder(id: Int) =
        updateReminders { it.filterNot { it.first == id }.toSet() }

    private fun upsertHabit(habit: Habit, schedule: HabitSchedule, isNew: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNew) habitsRepository.createHabit(habit, schedule)
            else habitsRepository.updateHabit(habit,schedule)
            _editorUiState.value = HabitEditorUiState.Saved
        }
    }

    private fun loadHabit(habitId: String) {
        _editorUiState.value = HabitEditorUiState.Loading
        viewModelScope.launch {
            habitsRepository.fetchHabitById(habitId)?.let { habit ->
                _editorUiState.value = HabitEditorUiState.Editing(
                    name = habit.name,
                    description = habit.description,
                    iconEmoji = habit.iconEmoji,
                    themeColor = habit.themeColor,
                    dailyGoal = habit.schedule.dailyGoal,
                    interval = habit.schedule.repeatInterval,
                    intervalCount = habit.schedule.repeatIntervalCount,
                    selectedDays = habit.schedule.recurrenceDays,
                    scheduleId = habit.schedule.id,
                    isNewHabit = false
                )
            } ?: run {
                _editorUiState.value = HabitEditorUiState.Editing()
            }
        }
    }

    private fun updateEditingState(update: (HabitEditorUiState.Editing) -> HabitEditorUiState.Editing) {
        _editorUiState.update { if (it is HabitEditorUiState.Editing) update(it) else it }
    }

    private fun updateUserMessage(@StringRes messageResId: Int?) =
        updateEditingState {
            it.copy(userMessage = messageResId)
        }

    private fun updateReminders(update: (Set<Pair<Int, LocalTime>>) -> Set<Pair<Int, LocalTime>>) =
        updateEditingState { it.copy(reminders = update(it.reminders)) }

    private fun Set<DayOfWeek>.toggle(day: DayOfWeek): Set<DayOfWeek> =
        if (contains(day)) minus(day) else plus(day)
}

sealed interface HabitEditorUiState {
    data object Loading : HabitEditorUiState
    data object Saved : HabitEditorUiState

    data class Editing(
        val name: String = "",
        val description: String = "",
        val iconEmoji: String? = null,
        val themeColor: Int? = null,
        val dailyGoal: Int? = 1,
        val interval: ScheduleFrequency = ScheduleFrequency.DAILY,
        val intervalCount: Int? = 1,
        val selectedDays: Set<DayOfWeek> = emptySet(),
        val reminders: Set<Pair<Int, LocalTime>> = setOf(Pair(0, LocalTime.now())),
        val scheduleId: Long = 0,
        val isNewHabit: Boolean = true,
        val userMessage: Int? = null
    ) : HabitEditorUiState {
        fun validate(): Int? {
            return when {
                name.isBlank() -> R.string.error_missing_habit_name
                iconEmoji.isNullOrBlank() -> R.string.error_missing_habit_icon
                themeColor == null -> R.string.error_missing_habit_color
                intervalCount == null -> R.string.error_missing_interval_count
                dailyGoal == null -> R.string.error_missing_daily_goal
                else -> null
            }
        }
    }
}