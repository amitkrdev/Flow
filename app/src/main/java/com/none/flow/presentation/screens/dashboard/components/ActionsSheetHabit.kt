package com.none.flow.presentation.screens.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.none.flow.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitActionsSheet(
    onEditHabit: () -> Unit,
    onArchiveHabit: () -> Unit,
    onReorderHabits: () -> Unit,
    onDismissSheet: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismissSheet() },
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = { Text(text = stringResource(id = R.string.action_edit_habit)) },
            leadingContent = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = stringResource(id = R.string.action_edit_habit)
                )
            },
            colors =
                ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
            modifier = Modifier.clickable { onEditHabit(); onDismissSheet()  }
        )
//        ListItem(
//            headlineContent = { Text(text = stringResource(id = R.string.action_reorder)) },
//            leadingContent = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_arrows_up_down),
//                    contentDescription = stringResource(id = R.string.action_reorder)
//                )
//            },
//            colors =
//                ListItemDefaults.colors(
//                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
//                ),
//            modifier = Modifier.clickable { onReorderHabits(); onDismissSheet() }
//        )
        ListItem(
            headlineContent = { Text(text = stringResource(id = R.string.action_archive_habit)) },
            leadingContent = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_archive),
                    contentDescription = stringResource(id = R.string.action_archive_habit)
                )
            },
            colors =
                ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
            modifier = Modifier.clickable { onArchiveHabit(); onDismissSheet() }
        )
    }
}
