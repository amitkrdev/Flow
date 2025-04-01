package com.none.flow.presentation.screens.reorder

import android.view.View
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.none.flow.R
import com.none.flow.data.model.Habit
import com.none.flow.data.model.UserHabit
import com.none.flow.presentation.components.PlaceholderScreen
import com.none.flow.presentation.dragdrop.DraggableItem
import com.none.flow.presentation.dragdrop.dragContainer
import com.none.flow.presentation.dragdrop.rememberDragDropState
import com.none.flow.presentation.screens.archived.components.ArchivedHabitCard
import com.none.flow.presentation.screens.dashboard.components.HabitCard
import com.none.flow.presentation.screens.reorder.components.ReorderHabitCard

@Preview(showBackground = true)
@Composable
fun PreviewReorderHabitsScreen() {
    ReorderHabitsScreen(
        uiState = ReorderHabitsUiState.Empty,
        onMoveHabit = { _, _ -> },
        onCloseScreen = {}
    )
}

@Composable
internal fun ReorderHabitsScreen(
    onCloseScreen: () -> Unit,
    viewModel: ReorderHabitsViewModel = hiltViewModel()
) {
    val reorderHabitsUiState by viewModel.reorderHabitsUiState.collectAsStateWithLifecycle()
    ReorderHabitsScreen(
        uiState = reorderHabitsUiState,
        onMoveHabit = viewModel::moveHabit,
        onCloseScreen = onCloseScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReorderHabitsScreen(
    uiState: ReorderHabitsUiState,
    onMoveHabit: (habitId: String, targetIndex: Int) -> Unit,
    onCloseScreen: () -> Unit
) {
    val view = LocalView.current
    val contentState = rememberLazyListState()
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            ReorderHabitsAppBar(
                scrollEffect = appBarScrollBehavior,
                onClose = onCloseScreen
            )
        }
    ) { paddingValues ->
        when (uiState) {
            ReorderHabitsUiState.Empty -> {
                PlaceholderScreen(
                    iconResId = R.drawable.ic_arrows_up_down,
                    titleResId = R.string.reorder_empty_title,
                    subtitleResId = R.string.reorder_empty_subtitle,
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues)
                )
            }

            ReorderHabitsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ReorderHabitsUiState.Success -> {
                ReorderHabitsContent(
                    view = view,
                    habitsList = uiState.habits,
                    contentState = contentState,
                    onMoveHabit = onMoveHabit,
                    modifier = Modifier
                        .padding(paddingValues)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReorderHabitsAppBar(
    modifier: Modifier = Modifier,
    scrollEffect: TopAppBarScrollBehavior,
    onClose: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.feature_reorder_habits)) },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.action_close)
                )
            }
        },
        scrollBehavior = scrollEffect,
        modifier = modifier
    )
}

@Composable
private fun ReorderHabitsContent(
    view: View,
    habitsList: List<Habit>,
    contentState: LazyListState,
    onMoveHabit: (habitId: String, targetIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
//    var list by remember { mutableStateOf(habitsList) }

    val dragDropState =
        rememberDragDropState(contentState) { fromIndex, toIndex ->
//            list = list.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
//            onMoveHabit(fromIndex, toIndex)
            val habitId = habitsList[fromIndex].id
            onMoveHabit(habitId, toIndex)
        }

    LazyColumn(
        modifier = Modifier.dragContainer(view, dragDropState),
        state = contentState,
    ) {
        itemsIndexed(habitsList, key = { _, item -> item.id }) { index, item ->
            DraggableItem(dragDropState, index) { isDragging ->
                ReorderHabitCard(habit = item, isDragging = isDragging)
            }
        }
    }
}
