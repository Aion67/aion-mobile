package com.example.enaf.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.components.EnafTopAppBar
import com.example.enaf.ui.theme.EnafActionBlue
import com.example.enaf.ui.theme.EnafBorder
import com.example.enaf.ui.theme.EnafCardBg
import com.example.enaf.ui.theme.EnafDarkBg
import com.example.enaf.ui.theme.EnafErrorRed
import com.example.enaf.ui.theme.EnafHeaderBg
import com.example.enaf.ui.theme.EnafHeaderBorder
import com.example.enaf.ui.theme.EnafProgressBg
import com.example.enaf.ui.theme.EnafTextSecondary
import com.example.enaf.ui.theme.EnafTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState = settingsPreviewState(),
    onEvent: (SettingsUiEvent) -> Unit = {},
) {
    Scaffold(
        topBar = { EnafTopAppBar() },
        containerColor = EnafDarkBg,
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item { ProfileCard(uiState.profile) }

            item {
                SettingsSection(
                    title = "Anti-Procrastination",
                    icon = Icons.Default.Warning,
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ToggleSettingItem(
                            title = "Regret Simulation",
                            description = "Sends reminders focused on the long-term cost of inaction.",
                            checked = uiState.regretSimulationEnabled,
                            onCheckedChange = { onEvent(SettingsUiEvent.RegretSimulationChanged(it)) },
                        )
                        ToggleSettingItem(
                            title = "Opportunity Leak",
                            description = "Alerts you when your inactivity window hits critical levels.",
                            checked = uiState.opportunityLeakEnabled,
                            onCheckedChange = { onEvent(SettingsUiEvent.OpportunityLeakChanged(it)) },
                        )
                    }
                }
            }

            item {
                SettingsSection(
                    title = "Notifications",
                    icon = Icons.Default.Notifications,
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text("Quiet Mode Duration", color = Color.White, fontSize = 14.sp)
                                Text(
                                    "${uiState.quietModeDurationHours.toInt()} Hours",
                                    color = EnafActionBlue,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Slider(
                                value = uiState.quietModeDurationHours,
                                onValueChange = { onEvent(SettingsUiEvent.QuietDurationChanged(it)) },
                                valueRange = 0f..8f,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color.White,
                                    activeTrackColor = EnafActionBlue,
                                    inactiveTrackColor = EnafProgressBg,
                                ),
                            )
                        }

                        ToggleSettingItem(
                            title = "Smart Alerts",
                            checked = uiState.smartAlertsEnabled,
                            onCheckedChange = { onEvent(SettingsUiEvent.SmartAlertsChanged(it)) },
                        )
                        ToggleSettingItem(
                            title = "Pulse Notifications",
                            checked = uiState.pulseNotificationsEnabled,
                            onCheckedChange = { onEvent(SettingsUiEvent.PulseNotificationsChanged(it)) },
                        )
                    }
                }
            }

            item {
                SettingsSection(
                    title = "Appearance",
                    icon = Icons.Default.Settings,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        AppearanceModeItem(
                            label = "Light",
                            icon = Icons.Default.Star,
                            selected = uiState.selectedTheme == ThemeModeOption.LIGHT,
                            onClick = { onEvent(SettingsUiEvent.ThemeSelected(ThemeModeOption.LIGHT)) },
                            modifier = Modifier.weight(1f),
                        )
                        AppearanceModeItem(
                            label = "Dark",
                            icon = Icons.Default.Info,
                            selected = uiState.selectedTheme == ThemeModeOption.DARK,
                            onClick = { onEvent(SettingsUiEvent.ThemeSelected(ThemeModeOption.DARK)) },
                            modifier = Modifier.weight(1f),
                        )
                        AppearanceModeItem(
                            label = "Celestial",
                            icon = Icons.Default.Face,
                            selected = uiState.selectedTheme == ThemeModeOption.CELESTIAL,
                            onClick = { onEvent(SettingsUiEvent.ThemeSelected(ThemeModeOption.CELESTIAL)) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { onEvent(SettingsUiEvent.SignOutClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EnafHeaderBg),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EnafHeaderBorder),
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = EnafErrorRed)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Sign Out", color = EnafErrorRed, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileCard(profile: SettingsProfileUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafCardBg, RoundedCornerShape(16.dp))
            .border(1.dp, EnafBorder, RoundedCornerShape(16.dp))
            .padding(25.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(EnafHeaderBg)
                .border(2.dp, EnafActionBlue, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color.Gray)
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column {
            Text(text = profile.displayName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = profile.email, color = EnafTextSecondary, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(EnafActionBlue.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
            ) {
                Text(text = profile.tierLabel, color = EnafActionBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = EnafActionBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(EnafHeaderBg.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .border(1.dp, EnafHeaderBorder, RoundedCornerShape(16.dp))
                .padding(16.dp),
        ) {
            content()
        }
    }
}

@Composable
fun ToggleSettingItem(
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            if (description != null) {
                Text(text = description, color = EnafTextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = EnafActionBlue,
            ),
        )
    }
}

@Composable
fun AppearanceModeItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(98.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) EnafActionBlue else EnafHeaderBorder,
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) EnafActionBlue.copy(alpha = 0.1f) else Color.Transparent,
        ),
        contentPadding = PaddingValues(8.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(if (selected) EnafActionBlue else EnafCardBg, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, color = Color.White, fontSize = 12.sp)
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    EnafTheme(darkTheme = true) {
        SettingsScreen()
    }
}
