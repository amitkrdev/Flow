package com.none.flow.presentation.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.none.flow.R
import com.none.flow.data.model.UserHabit
import com.none.flow.presentation.components.PlaceholderScreen
import com.none.flow.presentation.dragdrop.dragContainer
import com.none.flow.presentation.dragdrop.rememberDragDropState
import com.none.flow.presentation.screens.dashboard.components.HabitActionsSheet
import com.none.flow.presentation.screens.dashboard.components.HabitCard
import com.none.flow.presentation.screens.dashboard.components.NewHabitFab
import com.none.flow.utils.SnackbarManager
import com.none.flow.utils.isScrollingUp

@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    DashboardScreen(
        onEditHabit = {},
        onOpenSettings = {},
        onOpenArchived = {},
        onOpenReorderHabits = {},
        onViewStatistics = {}
    )
}

@Composable
fun DashboardScreen(
    onEditHabit: (String?) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenArchived: () -> Unit,
    onOpenReorderHabits: () -> Unit,
    onViewStatistics: (String?) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val feedUiState by viewModel.feedUiState.collectAsStateWithLifecycle()
    DashboardScreen(
        uiState = feedUiState,
        isUndoArchiveOptionVisible = viewModel.isUndoArchiveOptionVisible,
        onEditHabit = onEditHabit,
        onArchiveHabit = viewModel::archiveHabit,
        onUndoArchive = viewModel::undoLastArchivedHabit,
        onResetUndoArchiveState = viewModel::resetUndoArchiveState,
        onOpenArchived = onOpenArchived,
        onOpenSettings = onOpenSettings,
        onOpenReorderHabits = onOpenReorderHabits,
        onViewStatistics = onViewStatistics,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: HabitsFeedUiState,
    isUndoArchiveOptionVisible: Boolean = false,
    onEditHabit: (String?) -> Unit,
    onArchiveHabit: (String) -> Unit,
    onUndoArchive: () -> Unit = {},
    onResetUndoArchiveState: () -> Unit = {},
    onViewStatistics: (String?) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenArchived: () -> Unit,
    onOpenReorderHabits: () -> Unit,
) {
    val haptics = LocalHapticFeedback.current

    val contentState = rememberLazyListState()
    val appBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var selectedHabitForContextMenu by rememberSaveable { mutableStateOf<String?>(null) }

    val archiveUndoMessage = stringResource(id = R.string.undo_archive_message)
    val undoActionText = stringResource(id = R.string.action_undo)

    LaunchedEffect(isUndoArchiveOptionVisible) {
        if (isUndoArchiveOptionVisible) {
            val snackBarResult = SnackbarManager.showMessage(archiveUndoMessage, undoActionText)
            if (snackBarResult) {
                onUndoArchive()
            } else {
                onResetUndoArchiveState()
            }
        }
    }

    Scaffold(
        topBar = {
            DashboardAppBar(
                scrollEffect = appBarScrollBehavior,
                onOpenArchived = onOpenArchived,
                onOpenSettings = onOpenSettings,
                onViewStatistics = { onViewStatistics(null) }
            )
        },
        floatingActionButton = {
            NewHabitFab(
                onNewHabitClicked = { onEditHabit(null) },
                isExpanded = contentState.isScrollingUp()
            )
        },
        modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection),
    ) { paddingValues ->

        when (uiState) {
            HabitsFeedUiState.Loading -> Unit
            is HabitsFeedUiState.Success -> {
                DashboardContent(
                    habitCollection = uiState.userHabits,
                    onHabitLongPressed = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        selectedHabitForContextMenu = it
                    },
                    contentState = contentState,
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues)
                )

                if (selectedHabitForContextMenu != null) {
                    HabitActionsSheet(
                        onReorderHabits = onOpenReorderHabits,
                        onEditHabit = { onEditHabit(selectedHabitForContextMenu) },
                        onArchiveHabit = { onArchiveHabit(selectedHabitForContextMenu!!) },
                        onDismissSheet = { selectedHabitForContextMenu = null }
                    )
                }
            }

            HabitsFeedUiState.Empty -> {
                PlaceholderScreen(
                    iconResId = R.drawable.ic_task,
                    titleResId = R.string.dashboard_empty_title,
                    subtitleResId = R.string.dashboard_empty_subtitle,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardAppBar(
    onOpenArchived: () -> Unit,
    onOpenSettings: () -> Unit,
    onViewStatistics: () -> Unit,
    scrollEffect: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = onViewStatistics) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chart),
                    contentDescription = stringResource(id = R.string.action_view_analytics)
                )
            }
        },
        actions = {
            IconButton(onClick = onOpenArchived) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_archive),
                    contentDescription = stringResource(id = R.string.action_open_archives)
                )
            }

            IconButton(onClick = onOpenSettings) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = stringResource(id = R.string.action_open_settings)
                )
            }
        },
        scrollBehavior = scrollEffect
    )
}

@Composable
fun DashboardContent(
    contentState: LazyListState,
    habitCollection: List<UserHabit>,
    onHabitLongPressed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyColumn(
            state = contentState,
        ) {
            items(items = habitCollection, key = { it.id }) { habit ->
                HabitCard(
                    habit = habit,
                    onHabitClick = {},
                    onHabitLongPress = { onHabitLongPressed(habit.id) },
                    modifier = Modifier
                        .animateItem()
                )
            }
        }
    }
}

