package com.none.flow.presentation.screens.habiteditor.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.none.flow.R
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimePickerDialog(
    initialTime: LocalTime = LocalTime.now(),
    onTimeSelected: (LocalTime) -> Unit,
    onDismissDialog: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute
    )

    val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
    AlertDialog(
        onDismissRequest = onDismissDialog,
        text = {
            Column {
                TimePickerDialogHeader()
                TimePicker(state = timePickerState)
            }
        },
        confirmButton = {
            TextButton(onClick = { onTimeSelected(selectedTime); onDismissDialog() }) {
                Text(text = stringResource(id = R.string.action_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissDialog) {
                Text(text = stringResource(id = R.string.action_cancel))
            }
        },
    )
}

@Composable
private fun TimePickerDialogHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
    ) {
        Text(
            text = stringResource(id = R.string.dialog_title_select_time),
            style = MaterialTheme.typography.labelMedium
        )
    }
}