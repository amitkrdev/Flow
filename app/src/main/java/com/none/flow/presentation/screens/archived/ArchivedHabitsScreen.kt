package com.none.flow.presentation.screens.archived

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.none.flow.R
import com.none.flow.data.model.Habit
import com.none.flow.presentation.components.PlaceholderScreen
import com.none.flow.presentation.screens.archived.components.ArchivedHabitCard
import com.none.flow.presentation.screens.archived.components.ConfirmHabitDeletionDialog
import com.none.flow.utils.SnackbarManager

@Preview(showBackground = true)
@Composable
fun PreviewArchivedHabitsScreen() {
    val mockHabits = setOf("habit1", "habit2", "habit3") // Mock selected habit IDs
    val mockArchiveState = ArchivedHabitsUiState.Empty

    ArchivedHabitsScreen(
        selectedHabits = mockHabits,
        onSelectHabit = { /* TODO: Handle habit selection */ },
        onDeselectHabit = { /* TODO: Handle habit deselection */ },
        archivedUiState = mockArchiveState,
        onClearAllSelections = { /* TODO: Handle clearing selection */ },
        onDeleteSelectedHabits = { /* TODO: Handle deleting selected habits */ },
        onRestoreSelectedHabits = {},
        onRestoreHabit = { /* TODO: Handle restoring habit */ },
        onCloseScreen = { /* TODO: Handle screen close */ }
    )
}

@Composable
fun ArchivedHabitsScreen(
    onCloseScreen: () -> Unit,
    viewModel: ArchiveViewModel = hiltViewModel()
) {
    val archivedUiState by viewModel.archivedHabitsUiState.collectAsStateWithLifecycle()
    val selectedHabits by viewModel.selectedHabitIds.collectAsState()
    ArchivedHabitsScreen(
        archivedUiState = archivedUiState,
        selectedHabits = selectedHabits,
        onSelectHabit = viewModel::selectHabit,
        onDeselectHabit = viewModel::deselectHabit,
        onRestoreHabit = viewModel::restoreHabitById,
        onDeleteSelectedHabits = viewModel::removeAllSelectedHabits,
        onRestoreSelectedHabits = viewModel::restoreAllSelectedHabits,
        onClearAllSelections = viewModel::clearAllSelections,
        onCloseScreen = onCloseScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivedHabitsScreen(
    archivedUiState: ArchivedHabitsUiState,
    selectedHabits: Set<String>,
    onRestoreHabit: (String) -> Unit,
    onSelectHabit: (String) -> Unit,
    onDeselectHabit: (String) -> Unit,
    onRestoreSelectedHabits: () -> Unit,
    onDeleteSelectedHabits: () -> Unit,
    onClearAllSelections: () -> Unit,
    onCloseScreen: () -> Unit,
) {
    val haptics = LocalHapticFeedback.current

    val contentState = rememberLazyListState()
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var selectedHabitId by rememberSaveable { mutableStateOf<String?>(null) }
    var isDeleteConfirmationDialogVisible by rememberSaveable { mutableStateOf(false) }

    val isSelectionActive = selectedHabits.isNotEmpty()

    val restoreHabitMessage = stringResource(id = R.string.prompt_restore_habit_message)
    val restoreActionText = stringResource(id = R.string.action_restore_habit)

    val selectDeselectHabit: (String) -> Unit = { habitId ->
        val isHabitSelected = selectedHabits.contains(habitId)

        if (isHabitSelected) {
            onDeselectHabit(habitId)
        } else {
            onSelectHabit(habitId)
        }
    }

    if (isSelectionActive) {
        BackHandler(
            onBack = onClearAllSelections
        )
    }

    LaunchedEffect(selectedHabitId) {
        if (selectedHabitId != null) {
            val snackBarResult = SnackbarManager.showMessage(restoreHabitMessage, restoreActionText)
            if (snackBarResult) {
                onRestoreHabit(selectedHabitId!!)
            } else {
                selectedHabitId = null
            }
        }
    }

    Scaffold(
        topBar = {
            ArchivedHabitsAppBar(
                scrollEffect = appBarScrollBehavior,
                isSelectionActive = isSelectionActive,
                onRestoreSelected = onRestoreSelectedHabits,
                onDeleteSelected = { isDeleteConfirmationDialogVisible = true },
                onClearSelection = onClearAllSelections,
                onClose = onCloseScreen
            )
        },
        modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        when (archivedUiState) {
            ArchivedHabitsUiState.Empty -> {
                PlaceholderScreen(
                    iconResId = R.drawable.ic_archive,
                    titleResId = R.string.archived_empty_title,
                    subtitleResId = R.string.archived_empty_subtitle,
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues)
                )
            }

            ArchivedHabitsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ArchivedHabitsUiState.Success -> {
                ArchivedHabitsContent(
                    habits = archivedUiState.habits,
                    contentState = contentState,
                    onHabitClicked = { habitId ->
                        if (isSelectionActive) {
                            selectDeselectHabit(habitId)
                        } else {
                            selectedHabitId = habitId
                        }
                    },
                    onHabitLongPressed = { habitId ->
                        selectDeselectHabit(habitId)
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    isHabitSelected = { it in selectedHabits },
                    modifier = Modifier
                        .padding(paddingValues)
                )
            }
        }

        if (isDeleteConfirmationDialogVisible) {
            ConfirmHabitDeletionDialog(
                onConfirmDeletion = onDeleteSelectedHabits,
                onDismissDialog = { isDeleteConfirmationDialogVisible = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivedHabitsAppBar(
    modifier: Modifier= Modifier,
    scrollEffect: TopAppBarScrollBehavior,
    isSelectionActive: Boolean,
    onRestoreSelected: () -> Unit,
    onDeleteSelected: () -> Unit,
    onClearSelection: () -> Unit,
    onClose: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.feature_archived_habits)) },
        navigationIcon = {
            IconButton(onClick = if (isSelectionActive) onClearSelection else onClose) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.action_close)
                )
            }
        },
        actions = {
            if (isSelectionActive) {
                IconButton(onClick = onRestoreSelected) {
                    Icon(
                        painter = painterResource(R.drawable.ic_restore),
                        contentDescription = stringResource(R.string.action_restore_habit)
                    )
                }

                IconButton(onClick = onDeleteSelected) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = stringResource(R.string.action_delete)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor =
                if (isSelectionActive) TopAppBarDefaults.topAppBarColors().scrolledContainerColor
                else TopAppBarDefaults.topAppBarColors().containerColor,
        ),
        scrollBehavior = scrollEffect,
        modifier  = modifier
    )
}

@Composable
fun ArchivedHabitsContent(
    habits: List<Habit>,
    contentState: LazyListState,
    isHabitSelected: (String) -> Boolean,
    onHabitClicked: (String) -> Unit,
    onHabitLongPressed: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        LazyColumn(
            state = contentState,
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(items = habits, key = { it.id }) { habit ->
                ArchivedHabitCard(
                    habit = habit,
                    onHabitClick = { onHabitClicked(habit.id) },
                    onHabitLongPress = {
                        onHabitLongPressed(habit.id)
                    },
                    isSelected = isHabitSelected(habit.id),
                    modifier = Modifier
                        .animateItem()
                )
            }
        }
    }
}