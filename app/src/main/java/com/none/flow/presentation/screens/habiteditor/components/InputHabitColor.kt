package com.none.flow.presentation.screens.habiteditor.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.none.flow.R
import com.none.flow.data.model.enums.ColorTheme
import com.none.flow.utils.toColor
import com.none.flow.utils.toColorTheme

@Composable
internal fun HabitColorPicker(
    selectedColor: Int?,
    onColorSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    availableColors: List<ColorTheme> = ColorTheme.entries.sortedBy { it.name }
) {
    ColorPickerDropdown(selectedColor, onColorSelected, availableColors, modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorPickerDropdown(
    selectedColor: Int?,
    onColorSelected: (Int) -> Unit,
    availableColors: List<ColorTheme>,
    modifier: Modifier = Modifier
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val selectedColorTheme = selectedColor?.let { availableColors.toColorTheme(selectedColor) }

    val selectedColorThemeText =
        selectedColorTheme?.let { stringResource(id = it.labelResId) } ?: ""

    ExposedDropdownMenuBox(
        expanded = isDropdownExpanded,
        onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedColorThemeText,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(text = stringResource(id = R.string.input_placeholder_habit_color)) },
            leadingIcon = selectedColorTheme?.let {
                {
                    ColorPreviewIndicator(color = colorResource(id = selectedColorTheme.colorResId))
                }
            }
            ,
            trailingIcon = { TrailingIcon(expanded = isDropdownExpanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
        )

        ExposedDropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            availableColors.forEach { colorTheme ->
                val colorValue = colorTheme.toColor()

                ColorPickerOption(
                    colorTheme = colorTheme,
                    onColorSelected = {
                        onColorSelected(colorValue)
                        isDropdownExpanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorPickerOption(
    colorTheme: ColorTheme,
    onColorSelected: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(text = stringResource(id = colorTheme.labelResId)) },
        leadingIcon = { ColorPreviewIndicator(color = colorResource(id = colorTheme.colorResId)) },
        onClick = onColorSelected,
        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
    )
}

@Composable
private fun ColorPreviewIndicator(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(28.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(color = color)
        }
    }
}
