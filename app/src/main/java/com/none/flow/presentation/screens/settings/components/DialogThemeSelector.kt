package com.none.flow.presentation.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.none.flow.R
import com.none.flow.data.model.enums.ThemeMode

@Composable
internal fun ThemeSelectionDialog(
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    themeOptions: List<ThemeMode> = ThemeMode.entries,
    onDismissDialog: () -> Unit
) {
    var selectedTheme by remember { mutableStateOf(currentTheme) }

//    Dialog(
//        onDismissRequest = onDismissDialog,
//        properties = DialogProperties()
//    ) {
//        Surface(
//            shape = MaterialTheme.shapes.extraLarge,
//            color = MaterialTheme.colorScheme.surface,
//            tonalElevation = 6.dp,
//            modifier = Modifier
//                    .width(IntrinsicSize.Min)
//                    .height(IntrinsicSize.Min)
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Box(
//                    modifier = Modifier.padding(horizontal = 24.dp).padding(top = 24.dp, bottom = 16.dp),
//
//                ) {
//                    Text(text = stringResource(id = R.string.dialog_title_select_theme), style = MaterialTheme.typography.headlineMedium)
//                }
//                ThemeSelectionList(
//                    themeOptions = themeOptions,
//                    selectedTheme = selectedTheme,
//                    onThemeSelected = { selectedTheme = it }
//                )
//
//                Row(
//                    modifier = Modifier.padding(all = 24.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
//                ) {
//                    TextButton(onClick = onDismissDialog) {
//                        Text(text = stringResource(id = R.string.action_cancel))
//                    }
//                    TextButton(onClick = { onThemeSelected(selectedTheme); onDismissDialog() }) {
//                        Text(text = stringResource(id = R.string.action_apply))
//                    }
//                }
//            }
//        }
//    }
    AlertDialog(
            onDismissRequest = onDismissDialog,
    title = { Text(text = stringResource(id = R.string.dialog_title_select_theme)) },
    text = {
        ThemeSelectionList(
                    themeOptions = themeOptions,
                    selectedTheme = selectedTheme,
                    onThemeSelected = { selectedTheme = it }
                )
    },
    confirmButton = {
        TextButton(onClick = { onThemeSelected(selectedTheme); onDismissDialog() }) {
            Text(text = stringResource(id = R.string.action_apply))
        }
    },
    dismissButton = {
        TextButton(onClick = onDismissDialog) {
            Text(text = stringResource(id = R.string.action_cancel))
        }
    }
    )
}

@Composable
private fun ThemeSelectionList(
    themeOptions: List<ThemeMode>,
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit
) {
    Column {
        themeOptions.forEach { theme ->
            ThemeSelectionItem(
                themeMode = theme,
                isSelected = theme == selectedTheme,
                onThemeClicked = { onThemeSelected(theme) }
            )
        }
    }
}

@Composable
private fun ThemeSelectionItem(
    themeMode: ThemeMode,
    isSelected: Boolean,
    onThemeClicked: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .selectable(
                selected = isSelected,
                onClick = onThemeClicked,
                role = Role.RadioButton
            )
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = stringResource(id = themeMode.labelResId),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}