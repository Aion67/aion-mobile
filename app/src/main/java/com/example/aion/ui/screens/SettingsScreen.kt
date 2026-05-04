package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.aion.ui.components.*
import com.example.aion.ui.theme.Variables
import com.example.aion.utils.PermissionUtils
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.aion.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    onThemeClick: () -> Unit,
    onAccentClick: () -> Unit,
    onProfileClick: () -> Unit,
    onFaqClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onVersionClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.updatePermissionStatus(
                    usage = PermissionUtils.hasUsageStatsPermission(context),
                    overlay = PermissionUtils.hasOverlayPermission(context)
                )
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "Settings",
                actions = {
                    AionProfileActionButton(
                        onClick = onProfileClick
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                AionSettingsSection(title = "Account") {
                    AionSettingsRow(
                        title = "Profile",
                        subtitle = "Update username and profile picture",
                        leadingIcon = Icons.Filled.Person,
                        onClick = onProfileClick
                    )
                }
            }

            item {
                AionSettingsSection(title = "General") {
                    AionSettingsRow(
                        title = "Notifications",
                        subtitle = "Turn on notifications",
                        leadingIcon = Icons.Filled.Notifications,
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = { viewModel.toggleNotifications(it) }
                    )
                    AionSettingsRow(
                        title = "Display over other Apps",
                        subtitle = "Required for time limit alerts",
                        leadingIcon = Icons.Filled.Layers,
                        checked = uiState.hasOverlayPermission,
                        onCheckedChange = {
                            context.startActivity(PermissionUtils.getOverlayIntent(context))
                        }
                    )
                    AionSettingsRow(
                        title = "Usage Access",
                        subtitle = "Required to track app usage",
                        leadingIcon = Icons.Filled.Assessment,
                        checked = uiState.hasUsageAccess,
                        onCheckedChange = {
                            context.startActivity(PermissionUtils.getUsageStatsIntent())
                        }
                    )
                }
            }

            item {
                AionSettingsSection(title = "Custom") {
                    AionSettingsRow(
                        title = "Theme",
                        subtitle = uiState.theme,
                        leadingIcon = Icons.Filled.Palette,
                        onClick = onThemeClick
                    )
                    AionSettingsRow(
                        title = "Accent color",
                        subtitle = uiState.accentColor,
                        leadingIcon = Icons.Filled.Palette,
                        onClick = onAccentClick
                    )
                }
            }

            item {
                AionSettingsSection(title = "About") {
                    AionSettingsRow(
                        title = "FAQ",
                        leadingIcon = Icons.AutoMirrored.Filled.HelpOutline,
                        onClick = onFaqClick
                    )
                    AionSettingsRow(
                        title = "Feedback",
                        leadingIcon = Icons.Filled.Feedback,
                        onClick = onFeedbackClick
                    )
                    AionSettingsRow(
                        title = "Privacy Policy",
                        leadingIcon = Icons.Filled.PrivacyTip,
                        onClick = onPrivacyClick
                    )
                    AionSettingsRow(
                        title = "Version",
                        subtitle = "1.0.0",
                        leadingIcon = Icons.Filled.Info,
                        onClick = onVersionClick
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeSettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "Theme",
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onLeadingClick = onBack,
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Choose light or dark mode for the app.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = Variables.StaticBodyLargeSize,
                    lineHeight = Variables.StaticBodyLargeLineHeight,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Normal
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AionThemeOption(
                    label = "Light",
                    previewColor = Color(0xFFECE6F0),
                    isSelected = uiState.theme == "Light",
                    onClick = { viewModel.updateTheme("Light") },
                    modifier = Modifier.weight(1f)
                )

                AionThemeOption(
                    label = "Dark",
                    previewColor = Color(0xFF1D1B20),
                    isSelected = uiState.theme == "Dark",
                    onClick = { viewModel.updateTheme("Dark") },
                    modifier = Modifier.weight(1f)
                )
            }

            AionThemeOption(
                label = "System",
                previewColor = Color.Gray,
                isSelected = uiState.theme == "System",
                onClick = { viewModel.updateTheme("System") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AccentSettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val accentOptions = remember {
        listOf(
            AccentOption("Purple", Variables.PrimaryBrand),
            AccentOption("Pink", Variables.InstagramPink),
            AccentOption("Green", Variables.EmeraldGreen),
            AccentOption("Blue", Variables.SelectionBlue),
            AccentOption("Orange", Color(0xFFFF9800)),
            AccentOption("Teal", Color(0xFF14B8A6)),
            AccentOption("Red", Variables.WarningRed),
            AccentOption("Gray", Variables.SchemesOutline)
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "Accent color",
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onLeadingClick = onBack,
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Choose the accent color used for buttons and highlights.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = Variables.StaticBodyLargeSize,
                    lineHeight = Variables.StaticBodyLargeLineHeight,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Normal
                )
            )

            accentOptions.chunked(4).forEach { rowOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowOptions.forEach { option ->
                        AionAccentColorOption(
                            color = option.color,
                            label = option.label,
                            isSelected = uiState.accentColor == option.label,
                            onClick = { viewModel.updateAccentColor(option.label) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private data class AccentOption(
    val label: String,
    val color: Color,
)

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        onThemeClick = {},
        onAccentClick = {},
        onProfileClick = {},
        onFaqClick = {},
        onFeedbackClick = {},
        onPrivacyClick = {},
        onVersionClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ThemeSettingsScreenPreview() {
    ThemeSettingsScreen(onBack = {})
}

@Preview(showBackground = true)
@Composable
private fun AccentSettingsScreenPreview() {
    AccentSettingsScreen(onBack = {})
}
