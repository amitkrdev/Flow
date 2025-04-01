package com.none.flow.presentation.screens.habiteditor.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.none.flow.data.model.enums.ScheduleFrequency
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
import kotlin.enums.EnumEntries

@Composable
internal fun HabitScheduleInput(
    intervalCount: Int?,
    onIntervalCountChanged: (Int?) -> Unit,
    currentInterval: ScheduleFrequency,
    onIntervalChanged: (ScheduleFrequency) -> Unit,
    selectedDaysOfWeek: Set<DayOfWeek>,
    onWeekdayToggle: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier,
    availableIntervals: EnumEntries<ScheduleFrequency> = ScheduleFrequency.entries
) {
    Column(
        modifier = modifier,
    ) {
        Row {
            IntervalCountInputField(intervalCount, onIntervalCountChanged)
            Spacer(modifier = Modifier.width(12.dp))
            IntervalDropdownMenu(
                currentIntervalCount = intervalCount ?: 1,
                currentInterval = currentInterval,
                onIntervalChanged = onIntervalChanged,
                availableIntervals = availableIntervals,
                modifier = Modifier.weight(1f)
            )
        }
        AnimatedVisibility(
            visible = currentInterval == ScheduleFrequency.WEEKLY,
        ) {
            Box(modifier = Modifier.padding(top = 12.dp)) {
                WeeklyDaySelector(selectedDaysOfWeek, onWeekdayToggle)
            }
        }
    }
}

@Composable
private fun IntervalCountInputField(
    currentIntervalCount: Int?,
    onIntervalCountChanged: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = currentIntervalCount?.toString() ?: "",
        onValueChange = { input ->
            onIntervalCountChanged(input.filter { it.isDigit() }.toIntOrNull())
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = modifier.widthIn(max = 88.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntervalDropdownMenu(
    currentIntervalCount: Int,
    currentInterval: ScheduleFrequency,
    onIntervalChanged: (ScheduleFrequency) -> Unit,
    availableIntervals: EnumEntries<ScheduleFrequency>,
    modifier: Modifier = Modifier
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val selectedIntervalText =
        pluralStringResource(id = currentInterval.labelResId, count = currentIntervalCount)

    ExposedDropdownMenuBox(
        expanded = isDropdownExpanded,
        onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedIntervalText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { TrailingIcon(expanded = isDropdownExpanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
        )

        ExposedDropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            availableIntervals.forEach { interval ->
                IntervalDropdownOption(
                    intervalCount = currentIntervalCount,
                    interval = interval,
                    onIntervalSelected = {
                        onIntervalChanged(interval)
                        isDropdownExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun WeeklyDaySelector(
    selectedDaysOfWeek: Set<DayOfWeek>,
    onWeekdaySelectionChanged: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        DayOfWeek.entries.forEach { weekday ->
            val isSelected = weekday in selectedDaysOfWeek
            DaySelectionToggleButton(
                weekday = weekday,
                isSelected = isSelected,
                onToggle = { onWeekdaySelectionChanged(weekday) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntervalDropdownOption(
    intervalCount: Int,
    interval: ScheduleFrequency,
    onIntervalSelected: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(text = pluralStringResource(id = interval.labelResId, count = intervalCount))
        },
        onClick = onIntervalSelected,
        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
    )
}

@Composable
private fun DaySelectionToggleButton(
    weekday: DayOfWeek,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val displayName = weekday.getDisplayName(TextStyle.NARROW_STANDALONE, Locale.getDefault())

    OutlinedIconToggleButton(
        checked = isSelected,
        onCheckedChange = { onToggle() }
    ) {
        Text(displayName)
    }
}
