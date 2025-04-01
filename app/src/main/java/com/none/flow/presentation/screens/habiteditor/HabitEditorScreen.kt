package com.none.flow.presentation.screens.habiteditor

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.none.flow.R
import com.none.flow.data.model.enums.ScheduleFrequency
import com.none.flow.presentation.screens.habiteditor.components.EmojiPickerDialog
import com.none.flow.presentation.screens.habiteditor.components.HabitColorPicker
import com.none.flow.presentation.screens.habiteditor.components.HabitDetailsInputField
import com.none.flow.presentation.screens.habiteditor.components.HabitFormGroup
import com.none.flow.presentation.screens.habiteditor.components.HabitGoalInput
import com.none.flow.presentation.screens.habiteditor.components.HabitIconPicker
import com.none.flow.presentation.screens.habiteditor.components.HabitNameInputField
import com.none.flow.presentation.screens.habiteditor.components.HabitReminderInput
import com.none.flow.presentation.screens.habiteditor.components.HabitScheduleInput
import com.none.flow.presentation.screens.habiteditor.components.TimePickerDialog
import com.none.flow.utils.SnackbarManager
import java.time.DayOfWeek
import java.time.LocalTime

@Preview(showBackground = true)
@Composable
fun PreviewHabitEditorScreen() {
    HabitEditorScreen(
        editorUiState = HabitEditorUiState.Editing(),
        onHabitNameChanged = {},
        onHabitDescriptionChanged = {},
        onHabitIconChanged = {},
        onHabitColorChanged = {},
        onHabitGoalChanged = {},
        onReminderAdded = {},
        onReminderDeleted = {},
        onIntervalCountChanged = {},
        onIntervalChanged = {},
        onWeekdayToggle = {},
        onUserMessageShown = {},
        onSaveHabit = {},
        onHabitUpdate = {},
        onCloseScreen = {}
    )
}

@Composable
internal fun HabitEditorScreen(
    onHabitUpdate: () -> Unit,
    onCloseScreen: () -> Unit,
    viewModel: HabitEditorViewModel = hiltViewModel()
) {
    val editorUiState by viewModel.editorUiState.collectAsStateWithLifecycle()
    HabitEditorScreen(
        editorUiState = editorUiState,
        onHabitNameChanged = viewModel::updateName,
        onHabitDescriptionChanged = viewModel::updateDescription,
        onHabitIconChanged = viewModel::updateIconEmoji,
        onHabitColorChanged = viewModel::updateThemeColor,
        onHabitGoalChanged = viewModel::updateDailyGoal,
        onReminderAdded = viewModel::addReminder,
        onReminderDeleted = viewModel::deleteReminder,
        onIntervalCountChanged = viewModel::updateIntervalCount,
        onIntervalChanged = viewModel::updateInterval,
        onWeekdayToggle = viewModel::toggleSelectedDay,
        onUserMessageShown = viewModel::snackbarMessageShown,
        onSaveHabit = viewModel::saveHabit,
        onHabitUpdate = onHabitUpdate,
        onCloseScreen = onCloseScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HabitEditorScreen(
    editorUiState: HabitEditorUiState,
    onHabitNameChanged: (String) -> Unit,
    onHabitDescriptionChanged: (String) -> Unit,
    onHabitIconChanged: (String) -> Unit,
    onHabitColorChanged: (Int) -> Unit,
    onHabitGoalChanged: (Int?) -> Unit,
    onReminderAdded: (LocalTime) -> Unit,
    onReminderDeleted: (Int) -> Unit,
    onIntervalCountChanged: (Int?) -> Unit,
    onIntervalChanged: (ScheduleFrequency) -> Unit,
    onWeekdayToggle: (DayOfWeek) -> Unit,
    onUserMessageShown: () -> Unit,
    onSaveHabit: () -> Unit,
    onHabitUpdate: () -> Unit,
    onCloseScreen: () -> Unit
) {
    val contentState = rememberLazyListState()
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var isTimePickerVisible by rememberSaveable { mutableStateOf(false) }
    var isIconEmojiPickerVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(editorUiState) {
        if (editorUiState is HabitEditorUiState.Saved) {
            onHabitUpdate()
        }
    }

    when (editorUiState) {
        is HabitEditorUiState.Editing -> {
            Scaffold(
                topBar = {
                    HabitEditorAppBar(
                        isNewHabit = editorUiState.isNewHabit,
                        scrollEffect = appBarScrollBehavior,
                        onSaveHabit = onSaveHabit,
                        onCancel = onCloseScreen
                    )
                },
                modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection),
            ) { paddingValues ->

                editorUiState.userMessage?.let { userMessage ->
                    val snackbarText = stringResource(userMessage)
                    LaunchedEffect(userMessage) {
                        SnackbarManager.showMessage(message = snackbarText)
                        onUserMessageShown()
                    }
                }

                HabitEditorContent(
                    editorState = editorUiState,
                    onHabitNameChanged = onHabitNameChanged,
                    onHabitDescriptionChanged = onHabitDescriptionChanged,
                    onHabitColorChanged = onHabitColorChanged,
                    onHabitGoalChanged = onHabitGoalChanged,
                    onReminderAdded = { isTimePickerVisible = true },
                    onReminderDeleted = onReminderDeleted,
                    onIntervalCountChanged = onIntervalCountChanged,
                    onIntervalChanged = onIntervalChanged,
                    onWeekdayToggle = onWeekdayToggle,
                    onIconSelected = { isIconEmojiPickerVisible = true },
                    contentState = contentState,
                    modifier = Modifier
                        .padding(paddingValues)
                        .imePadding()
                )

                if (isIconEmojiPickerVisible) {
                    EmojiPickerDialog(
                        selectedEmoji = editorUiState.iconEmoji,
                        onEmojiSelected = onHabitIconChanged,
                        onDismissDialog = { isIconEmojiPickerVisible = false }
                    )
                }

                if (isTimePickerVisible) {
                    TimePickerDialog(
                        onTimeSelected = onReminderAdded,
                        onDismissDialog = { isTimePickerVisible = false }
                    )
                }
            }
        }

        else -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HabitEditorAppBar(
    isNewHabit: Boolean,
    onSaveHabit: () -> Unit,
    onCancel: () -> Unit,
    scrollEffect: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(
                    id = if (isNewHabit)
                        R.string.feature_create_habit
                    else
                        R.string.feature_edit_habit
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onCancel) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(id = R.string.action_cancel)
                )
            }
        },
        actions = {
            TextButton(onClick = onSaveHabit) {
                Text(stringResource(id = R.string.action_save))
            }
        },
        scrollBehavior = scrollEffect,
        modifier = modifier
    )
}

@Composable
fun HabitEditorContent(
    editorState: HabitEditorUiState.Editing,
    onHabitNameChanged: (String) -> Unit,
    onHabitDescriptionChanged: (String) -> Unit,
    onHabitColorChanged: (Int) -> Unit,
    onHabitGoalChanged: (Int?) -> Unit,
    onReminderAdded: () -> Unit,
    onReminderDeleted: (Int) -> Unit,
    onIntervalCountChanged: (Int?) -> Unit,
    onIntervalChanged: (ScheduleFrequency) -> Unit,
    onWeekdayToggle: (DayOfWeek) -> Unit,
    onIconSelected: () -> Unit,
    contentState: LazyListState,
    modifier: Modifier = Modifier
) {
    val (
        habitName: String,
        habitDescription: String,
        selectedIconEmoji: String?,
        selectedColor: Int?,
        habitGoalCount: Int?,
        selectedInterval: ScheduleFrequency,
        habitIntervalCount: Int?,
        selectedWeekdays: Set<DayOfWeek>,
        habitReminders: Set<Pair<Int, LocalTime>>,
    ) = editorState

    Box(modifier = modifier) {
        LazyColumn(
            state = contentState,
            contentPadding = PaddingValues(
                vertical = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(space = 12.dp, alignment = Alignment.Top)
        ) {
            habitInfoSection(
                habitIconEmoji = selectedIconEmoji,
                habitName = habitName,
                habitDescription = habitDescription,
                habitGoalCount = habitGoalCount,
                onHabitGoalChanged = onHabitGoalChanged,
                onHabitNameChanged = onHabitNameChanged,
                onHabitDescriptionChanged = onHabitDescriptionChanged,
                selectedColor = selectedColor,
                onHabitColorChanged = onHabitColorChanged,
                onIconSelected = onIconSelected
            )

            habitScheduleSection(
                habitIntervalCount = habitIntervalCount,
                onIntervalCountChanged = onIntervalCountChanged,
                selectedInterval = selectedInterval,
                onIntervalChanged = onIntervalChanged,
                selectedWeekdays = selectedWeekdays,
                onWeekdayToggle = onWeekdayToggle
            )

            habitReminderSection(
                habitReminders = habitReminders,
                onReminderAdded = onReminderAdded,
                onReminderDeleted = onReminderDeleted
            )
        }
    }
}

private fun LazyListScope.habitInfoSection(
    habitIconEmoji: String?,
    onIconSelected: () -> Unit,
    habitName: String,
    onHabitNameChanged: (String) -> Unit,
    habitDescription: String,
    onHabitDescriptionChanged: (String) -> Unit,
    habitGoalCount: Int?,
    onHabitGoalChanged: (Int?) -> Unit,
    selectedColor: Int?,
    onHabitColorChanged: (Int) -> Unit
) {
    item {
        HabitIconPicker(
            selectedIconEmoji = habitIconEmoji,
            onSelectIcon = onIconSelected
        )
        Spacer(Modifier.height(4.dp))
    }

    habitFormGroup(titleResId = null) {
        HabitNameInputField(
            habitName,
            onHabitNameChanged
        )

        HabitDetailsInputField(
            habitDescription,
            onHabitDescriptionChanged
        )

        HabitColorPicker(
            selectedColor = selectedColor,
            onColorSelected = onHabitColorChanged
        )

        HabitGoalInput(
            goalValue = habitGoalCount,
            onGoalValueChanged = onHabitGoalChanged
        )
    }
}

private fun LazyListScope.habitScheduleSection(
    habitIntervalCount: Int?,
    onIntervalCountChanged: (Int?) -> Unit,
    selectedInterval: ScheduleFrequency,
    onIntervalChanged: (ScheduleFrequency) -> Unit,
    selectedWeekdays: Set<DayOfWeek>,
    onWeekdayToggle: (DayOfWeek) -> Unit,
) {
    habitFormGroup(titleResId = R.string.habit_form_section_schedule) {
        HabitScheduleInput(
            intervalCount = habitIntervalCount,
            onIntervalCountChanged = onIntervalCountChanged,
            currentInterval = selectedInterval,
            onIntervalChanged = onIntervalChanged,
            selectedDaysOfWeek = selectedWeekdays,
            onWeekdayToggle = onWeekdayToggle
        )
    }
}

private fun LazyListScope.habitReminderSection(
    habitReminders: Set<Pair<Int, LocalTime>>,
    onReminderAdded: () -> Unit,
    onReminderDeleted: (Int) -> Unit,
) {
    habitFormGroup(titleResId = R.string.habit_form_section_reminders) {
        HabitReminderInput(
            reminderList = habitReminders,
            onReminderAdded = onReminderAdded,
            onReminderRemoved = onReminderDeleted
        )
    }
}

private fun LazyListScope.habitFormGroup(
    @StringRes titleResId: Int?,
    sectionContent: @Composable ColumnScope.() -> Unit
) {
    item {
        HabitFormGroup(
            sectionTitleResId = titleResId,
            content = sectionContent
        )
    }
}