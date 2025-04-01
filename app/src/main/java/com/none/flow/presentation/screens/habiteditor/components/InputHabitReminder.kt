package com.none.flow.presentation.screens.habiteditor.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.none.flow.R
import com.none.flow.utils.toReminderTime
import java.time.LocalTime

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun HabitReminderInput(
    reminderList: Set<Pair<Int, LocalTime>>,
    onReminderAdded: () -> Unit,
    onReminderRemoved: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top),
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.Start
        ),
    ) {
        reminderList.forEach { (reminderId, reminderTime) ->
            ReminderEntryChip(reminderId, reminderTime, onReminderRemoved)
        }
        NewReminderChip(onReminderAdded)
    }
}


@Composable
private fun NewReminderChip(onReminderAdded: () -> Unit) {
    AssistChip(
        onClick = onReminderAdded,
        label = { Text(stringResource(R.string.action_new_reminder)) },
        leadingIcon = {
            Icon(
                painterResource(R.drawable.ic_plus), contentDescription = null,
                modifier = Modifier.size(AssistChipDefaults.IconSize)
            )
        },
    )
}

@Composable
private fun ReminderEntryChip(
    reminderId: Int,
    reminderTime: LocalTime,
    onReminderRemoved: (Int) -> Unit
) {
    AssistChip(
        onClick = { /* Do something! */ },
        label = { Text(text = reminderTime.toReminderTime()) },
        trailingIcon = {
            Icon(
                painterResource(R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onReminderRemoved(reminderId) }
                    .size(AssistChipDefaults.IconSize)
            )
        }
    )
}
