package com.none.flow.presentation.screens.dashboard.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.none.flow.R

@Composable
fun NewHabitFab(
    onNewHabitClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = true
) {
    ExtendedFloatingActionButton(
        onClick = onNewHabitClicked,
        expanded = isExpanded,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable. ic_plus),
                contentDescription = stringResource(id = R.string.action_new_habit),
            )
        },
        text = {
            Text(text = stringResource(id = R.string.action_new_habit))
        },
        modifier = modifier,
    )
}
