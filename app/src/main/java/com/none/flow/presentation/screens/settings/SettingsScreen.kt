package com.none.flow.presentation.screens.settings

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.none.flow.BuildConfig
import com.none.flow.R
import com.none.flow.data.model.UserPreferences
import com.none.flow.data.model.enums.ThemeMode
import com.none.flow.presentation.screens.settings.components.AppInfoOption
import com.none.flow.presentation.screens.settings.components.SettingsCategoryOption
import com.none.flow.presentation.screens.settings.components.SettingsGroup
import com.none.flow.presentation.screens.settings.components.SettingsToggleOption
import com.none.flow.presentation.screens.settings.components.ThemeSelectionDialog
import com.none.flow.utils.AppConstants
import com.none.flow.utils.supportsDynamicTheming

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen(
        uiState = SettingsUiState.Success(
            settings = UserPreferences(
                theme = ThemeMode.SYSTEM,
                enableDynamicColor = false
            )
        ),
        onThemeChanged = {},
        onDynamicColorToggled = {},
        onRestoreBackup = {},
        onCreateBackup = {},
        onCloseScreen = {}
    )
}

@Composable
internal fun SettingsScreen(
    onCloseScreen: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = settingsUiState,
        onThemeChanged = viewModel::updateThemeMode,
        onDynamicColorToggled = viewModel::updateDynamicColorPreference,
        onRestoreBackup = viewModel::onRestoreBackup,
        onCreateBackup = viewModel::onCreateBackup,
        onCloseScreen = onCloseScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    uiState: SettingsUiState,
    onThemeChanged: (ThemeMode) -> Unit,
    onDynamicColorToggled: (Boolean) -> Unit,
    onCreateBackup: () -> Unit,
    onRestoreBackup: () -> Unit,
    isDynamicColorSupported: Boolean = supportsDynamicTheming(),
    onCloseScreen: () -> Unit,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    val contentState = rememberLazyListState()
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var isThemeSelectorVisible by rememberSaveable { mutableStateOf(false) }

    val shareApp: (String, String) -> Unit = { title, appUrl ->
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, appUrl)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, title))
    }

    val sendSupportEmail: (email: String, subject: String) -> Unit = { email, subject ->
        val mailIntent = Intent(
            Intent.ACTION_VIEW, "mailto:$email".toUri().buildUpon()
                .appendQueryParameter("subject", subject)
                .build()
        ).let {
            Intent.createChooser(it, "Choose an email client")
        }
        context.startActivity(mailIntent)
    }

    val viewLicenses: () -> Unit = {
        context.startActivity(Intent(context, OssLicensesMenuActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    Scaffold(
        topBar = { SettingsAppBar(onClose = onCloseScreen, scrollEffect = appBarScrollBehavior) },
        modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        when (uiState) {
            SettingsUiState.Loading -> Unit
            is SettingsUiState.Success -> {
                SettingsContent(
                    userSettings = uiState.settings,
                    isDynamicColorSupported = isDynamicColorSupported,
                    onDynamicColorToggled = onDynamicColorToggled,
                    onCreateBackup = onCreateBackup,
                    onRestoreBackup = onRestoreBackup,
                    onShareApp = shareApp,
                    onSendSupportEmail = sendSupportEmail,
                    onViewLicenses = viewLicenses,
                    onOpenExternalLink = uriHandler::openUri,
                    onThemeSelectorOpened = { isThemeSelectorVisible = true },
                    contentState = contentState,
                    contentPadding = paddingValues
                )

                if (isThemeSelectorVisible) {
                    ThemeSelectionDialog(
                        currentTheme = uiState.settings.theme,
                        onThemeSelected = onThemeChanged,
                        onDismissDialog = { isThemeSelectorVisible = false }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsAppBar(
    onClose: () -> Unit,
    scrollEffect: TopAppBarScrollBehavior?,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.feature_settings)) },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.action_close)
                )
            }
        },
        scrollBehavior = scrollEffect,
        modifier = modifier
    )
}

@Composable
private fun SettingsContent(
    userSettings: UserPreferences,
    isDynamicColorSupported: Boolean,
    onDynamicColorToggled: (Boolean) -> Unit,
    onCreateBackup: () -> Unit,
    onRestoreBackup: () -> Unit,
    onShareApp: (String, String) -> Unit,
    onSendSupportEmail: (email: String, subject: String) -> Unit,
    onOpenExternalLink: (link: String) -> Unit,
    onViewLicenses: () -> Unit,
    onThemeSelectorOpened: () -> Unit,
    contentState: LazyListState,
    contentPadding: PaddingValues
) {
    LazyColumn(state = contentState, contentPadding = contentPadding) {
        shareAppSection(onShareApp = onShareApp)

        appearanceSettings(
            themeMode = userSettings.theme,
            isDynamicColorEnabled = userSettings.enableDynamicColor,
            isDynamicColorSupported = isDynamicColorSupported,
            onDynamicColorChanged = onDynamicColorToggled,
            onThemeSelectorOpened = onThemeSelectorOpened
        )

        dataManagementSettings(
            onCreateBackup = onCreateBackup,
            onRestoreBackup = onRestoreBackup
        )

        legalAndSupportSettings(
            onSendSupportEmail = onSendSupportEmail,
            onOpenExternalLink = onOpenExternalLink,
            onViewLicenses = onViewLicenses
        )

        aboutAppSection()
    }
}

private fun LazyListScope.shareAppSection(onShareApp: (String, String) -> Unit) {
    settingsGroup(titleResId = null) {
        val shareTitle =
            stringResource(id = R.string.app_name) + " : " + stringResource(id = R.string.app_tagline)
        SettingsCategoryOption(
            iconResId = R.drawable.ic_gift,
            titleResId = R.string.settings_option_invite,
            onClick = { onShareApp(shareTitle, AppConstants.DOWNLOAD_LINK) }
        )
    }
}

private fun LazyListScope.appearanceSettings(
    themeMode: ThemeMode,
    isDynamicColorEnabled: Boolean,
    isDynamicColorSupported: Boolean,
    onDynamicColorChanged: (Boolean) -> Unit,
    onThemeSelectorOpened: () -> Unit,
) {
    settingsGroup(titleResId = R.string.settings_section_appearance) {
        SettingsCategoryOption(
            iconResId = R.drawable.ic_color_scheme,
            titleResId = R.string.settings_option_color_scheme,
            subtitleResId = themeMode.labelResId,
            onClick = onThemeSelectorOpened
        )

        if (isDynamicColorSupported) {
            SettingsToggleOption(
                iconResId = R.drawable.ic_palette,
                titleResId = R.string.settings_option_adaptive_colors,
                isToggledOn = isDynamicColorEnabled,
                onToggleChange = onDynamicColorChanged
            )
        }
    }
}

private fun LazyListScope.dataManagementSettings(
    onCreateBackup: () -> Unit,
    onRestoreBackup: () -> Unit
) {
    settingsGroup(titleResId = R.string.settings_section_data) {
        SettingsCategoryOption(
            iconResId = R.drawable.ic_export,
            titleResId = R.string.settings_option_backup_data,
            subtitleResId = R.string.settings_option_backup_data_desc,
            onClick = onCreateBackup
        )
        SettingsCategoryOption(
            iconResId = R.drawable.ic_import,
            titleResId = R.string.settings_option_restore_data,
            subtitleResId = R.string.settings_option_restore_data_desc,
            onClick = onRestoreBackup
        )
    }
}

private fun LazyListScope.legalAndSupportSettings(
    onSendSupportEmail: (String, String) -> Unit,
    onOpenExternalLink: (String) -> Unit,
    onViewLicenses: () -> Unit
) {
    settingsGroup(titleResId = R.string.settings_section_legal_support) {
        SettingsCategoryOption(
            iconResId = R.drawable.ic_mail,
            titleResId = R.string.support_contact,
            onClick = {
                onSendSupportEmail(
                    AppConstants.SUPPORT_EMAIL,
                    AppConstants.SUPPORT_EMAIL_SUBJECT
                )
            }
        )

        SettingsCategoryOption(
            iconResId = R.drawable.ic_description,
            titleResId = R.string.legal_terms_of_service,
            onClick = { onOpenExternalLink(AppConstants.TERMS_OF_SERVICE_URL) }
        )

        SettingsCategoryOption(
            iconResId = R.drawable.ic_lock,
            titleResId = R.string.legal_privacy,
            onClick = { onOpenExternalLink(AppConstants.PRIVACY_POLICY_URL) }
        )

        SettingsCategoryOption(
            iconResId = R.drawable.ic_handshake,
            titleResId = R.string.legal_open_source_licenses,
            onClick = onViewLicenses
        )
    }
}

private fun LazyListScope.aboutAppSection() {
    val developerName = AppConstants.DEVELOPER_NAME
    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

    settingsGroup(titleResId = R.string.settings_section_about_app) {
        AppInfoOption(
            appVersion = stringResource(id = R.string.about_app_version, appVersion),
            copyrightText = stringResource(
                id = R.string.about_developer_credit,
                developerName
            )
        )
    }
}

private fun LazyListScope.settingsGroup(
    @StringRes titleResId: Int?,
    content: @Composable ColumnScope.() -> Unit,
) {
    item { SettingsGroup(groupTitleResId = titleResId, content = content) }
}