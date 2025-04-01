package com.none.flow.presentation.screens.archived.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.none.flow.R

@Composable
fun ConfirmHabitDeletionDialog(
    onConfirmDeletion: () -> Unit,
    onDismissDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
        title = {
            Text(text = stringResource(id = R.string.dialog_title_delete_habits))
        },
        text = {
            Text(text = stringResource(id = R.string.dialog_message_delete_habits))
        },
        confirmButton = {
            TextButton(onClick = onConfirmDeletion) {
                Text(text = stringResource(id = R.string.action_delete),
                    )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissDialog) {
                Text(
                    text = stringResource(id = R.string.action_cancel),
                )
            }
        }
    )
}
