package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.components.AionAccentColorOption
import com.example.aion.ui.components.AionSettingsRow
import com.example.aion.ui.components.AionSettingsSection
import com.example.aion.ui.components.AionThemeOption
import com.example.aion.ui.components.AionTopAppBar
import com.example.aion.ui.theme.Variables
import androidx.compose.runtime.saveable.rememberSaveable
@Composable
fun SettingsScreen(
    onThemeClick: () -> Unit,
    onAccentClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "Settings"
            )
        },
        containerColor = Variables.SchemesSurface
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Variables.SchemesSurface),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
        ) {
            item {
                AionSettingsSection(
                    title = "General"
                ) {
                    AionSettingsRow(
                        title = "Notifications",
                        subtitle = "Turn on notifications",
                        leadingIcon = Icons.Filled.Notifications,
                        checked = true,
                        onCheckedChange = { }
                    )
                    AionSettingsRow(
                        title = "Display over other Apps",
                        subtitle = "Display over tracked apps when time is up",
                        leadingIcon = Icons.Filled.Apps,
                        checked = true,
                        onCheckedChange = { }
                    )
                }
            }

            item {
                AionSettingsSection(
                    title = "Custom"
                ) {
                    AionSettingsRow(
                        title = "Theme",
                        subtitle = "Choose light or dark mode",
                        leadingIcon = Icons.Filled.Palette,
                        onClick = onThemeClick
                    )
                    AionSettingsRow(
                        title = "Accent color",
                        subtitle = "Pick the highlight color used across the app",
                        leadingIcon = Icons.Filled.Palette,
                        onClick = onAccentClick
                    )
                }
            }

            item {
                AionSettingsSection(
                    title = "About"
                ) {
                    AionSettingsRow(
                        title = "FAQ",
                        leadingIcon = Icons.Filled.HelpOutline,
                        onClick = { }
                    )
                    AionSettingsRow(
                        title = "Feedback",
                        leadingIcon = Icons.Filled.Feedback,
                        onClick = { }
                    )
                    AionSettingsRow(
                        title = "Privacy Policy",
                        leadingIcon = Icons.Filled.PrivacyTip,
                        onClick = { }
                    )
                    AionSettingsRow(
                        title = "Version",
                        subtitle = "1.0.0",
                        leadingIcon = Icons.Filled.Info
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
) {
    var selectedThemeIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "Theme",
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onLeadingClick = onBack
            )
        },
        containerColor = Variables.SchemesSurface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Variables.SchemesSurface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "This is a them selection view",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = Variables.StaticBodyLargeSize,
                    lineHeight = Variables.StaticBodyLargeLineHeight,
                    color = Variables.SchemesOnSurfaceVariant,
                    fontWeight = FontWeight.Normal
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AionThemeOption(
                    label = "Light mode",
                    previewColor = Color(0xFFECE6F0),
                    isSelected = selectedThemeIndex == 0,
                    onClick = { selectedThemeIndex = 0 },
                    modifier = Modifier.weight(1f)
                )

                AionThemeOption(
                    label = "Dark mode",
                    previewColor = Color(0xFF1D1B20),
                    isSelected = selectedThemeIndex == 1,
                    onClick = { selectedThemeIndex = 1 },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AccentSettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedAccentIndex by rememberSaveable { mutableIntStateOf(0) }

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
                onLeadingClick = onBack
            )
        },
        containerColor = Variables.SchemesSurface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Variables.SchemesSurface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Choose the accent color used for buttons and highlights.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = Variables.StaticBodyLargeSize,
                    lineHeight = Variables.StaticBodyLargeLineHeight,
                    color = Variables.SchemesOnSurfaceVariant,
                    fontWeight = FontWeight.Normal
                )
            )

            accentOptions.chunked(4).forEachIndexed { rowIndex, rowOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowOptions.forEachIndexed { columnIndex, option ->
                        val optionIndex = rowIndex * 4 + columnIndex
                        AionAccentColorOption(
                            color = option.color,
                            label = option.label,
                            isSelected = selectedAccentIndex == optionIndex,
                            onClick = { selectedAccentIndex = optionIndex },
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
        onAccentClick = {}
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
