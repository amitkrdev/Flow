package com.none.flow.presentation.screens.settings.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.none.flow.R

@Composable
internal fun AppInfoOption(
    appVersion: String,
    copyrightText: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int = R.drawable.ic_info
) {
    ListItem(
        headlineContent = { Text(text = copyrightText) },
        supportingContent = { Text(text = appVersion) },
        leadingContent = { Icon(painter = painterResource(iconResId), contentDescription = null) },
        modifier = modifier
    )
}

@Composable
internal fun SettingsCategoryOption(
    @DrawableRes iconResId: Int,
    @StringRes titleResId: Int,
    modifier: Modifier = Modifier,
    @StringRes subtitleResId: Int? = null,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(text = stringResource(titleResId)) },
        supportingContent = subtitleResId?.let { { Text(text = stringResource(it)) } },
        leadingContent = { Icon(painter = painterResource(iconResId), contentDescription = null) },
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
internal fun SettingsToggleOption(
    @DrawableRes iconResId: Int,
    @StringRes titleResId: Int,
    modifier: Modifier = Modifier,
    @StringRes subtitleResId: Int? = null,
    isToggledOn: Boolean,
    onToggleChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(text = stringResource(titleResId)) },
        supportingContent = subtitleResId?.let { { Text(text = stringResource(it)) } },
        leadingContent = { Icon(painter = painterResource(iconResId), contentDescription = null) },
        trailingContent = {
            Switch(
                checked = isToggledOn,
                onCheckedChange = null
            )
        },
        modifier = modifier
            .toggleable(
                value = isToggledOn,
                onValueChange = onToggleChange,
                role = Role.Switch
            )
    )
}

@Composable
internal fun SettingsGroup(
    modifier: Modifier = Modifier,
    @StringRes groupTitleResId: Int?,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier) {
        groupTitleResId?.let { SettingsGroupHeader(it) }
        content()
    }
}

@Composable
private fun SettingsGroupHeader(
    @StringRes groupTitleResId: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = stringResource(groupTitleResId),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}