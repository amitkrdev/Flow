package com.none.flow.presentation.screens.habiteditor.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.none.flow.R

@Composable
internal fun HabitGoalInput(
    goalValue: Int?,
    onGoalValueChanged: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GoalInputField(goalValue, onGoalValueChanged, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(12.dp))
        GoalAdjustmentControls(goalValue, onGoalValueChanged)
    }
}

@Composable
private fun GoalInputField(
    goalValue: Int?,
    onGoalValueChanged: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = goalValue?.toString() ?: "",
        onValueChange = { input ->
            onGoalValueChanged(input.filter { it.isDigit() }.toIntOrNull())
        },
        suffix = { Text(text = stringResource(id = R.string.input_suffix_goal_daily)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun GoalAdjustmentControls(
    goalValue: Int?,
    onGoalValueChanged: (Int?) -> Unit
) {
    Row {
        GoalAdjustButton(
            iconResourceId = R.drawable.ic_minus,
            contentDescriptionResId = R.string.action_decrease_habit_goal,
            onClick = { onGoalValueChanged((goalValue ?: 1).coerceAtLeast(2) - 1) }
        )

        Spacer(modifier = Modifier.width(4.dp))

        GoalAdjustButton(
            iconResourceId = R.drawable.ic_plus,
            contentDescriptionResId = R.string.action_increase_habit_goal,
            onClick = { onGoalValueChanged((goalValue ?: 0) + 1) }
        )
    }
}

@Composable
private fun GoalAdjustButton(
    @DrawableRes iconResourceId: Int,
    @StringRes contentDescriptionResId: Int,
    onClick: () -> Unit
) {
    FilledTonalIconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = stringResource(id = contentDescriptionResId)
        )
    }
}
