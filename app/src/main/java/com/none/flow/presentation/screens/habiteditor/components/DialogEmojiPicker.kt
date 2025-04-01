package com.none.flow.presentation.screens.habiteditor.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import com.none.flow.R

@Composable
internal fun EmojiPickerDialog(
    selectedEmoji: String?,
    onEmojiSelected: (String) -> Unit,
    onDismissDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
        title = {
            Text(text = stringResource(id = R.string.dialog_title_select_icon))
        },
        text = {
            EmojiPickerView(
                selectedEmoji = selectedEmoji,
                onEmojiSelected = { onEmojiSelected(it); onDismissDialog() },
                modifier = Modifier
                    .fillMaxHeight(.7f)
            )
        },
        confirmButton = {
            TextButton(onClick = onDismissDialog) {
                Text(text = stringResource(id = R.string.action_cancel))
            }
        },
    )
}

@Composable
fun EmojiPickerView(
    selectedEmoji: String?,
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = {
            EmojiPickerView(it)
                .apply {
                    setOnEmojiPickedListener { item ->
                        if (selectedEmoji != item.emoji) {
                            onEmojiSelected(item.emoji)
                        }
                    }
                }
        },
    )
}