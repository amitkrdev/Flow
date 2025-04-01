package com.none.flow.presentation.screens.habiteditor.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.none.flow.R

@Composable
internal fun HabitNameInputField(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        placeholder = { Text(stringResource(R.string.input_placeholder_habit_name)) },
        trailingIcon = {
            if (name.isNotBlank()) {
                ClearTextButton(onClearText = { onNameChange("") })
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
internal fun HabitDetailsInputField(
    details: String,
    onDetailsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = details,
        onValueChange = onDetailsChange,
        placeholder = { Text(stringResource(R.string.input_placeholder_habit_description)) },
        trailingIcon = {
            if (details.isNotBlank()) {
                ClearTextButton(onClearText = { onDetailsChange("") })
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun ClearTextButton(onClearText: () -> Unit) {
    IconButton(onClick = onClearText) {
        Icon(
            painterResource(R.drawable.ic_clear),
            contentDescription = stringResource(R.string.action_clear)
        )
    }
}
