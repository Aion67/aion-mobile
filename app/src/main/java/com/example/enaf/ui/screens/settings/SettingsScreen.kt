package com.example.enaf.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.enaf.ui.theme.*

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            EnafTopAppBar()
        },
        containerColor = EnafDarkBg
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Card
            item {
                ProfileCard()
            }

            // Anti-Procrastination Section
            item {
                SettingsSection(
                    title = "Anti-Procrastination",
                    icon = Icons.Default.Warning
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ToggleSettingItem(
                            title = "Regret Simulation",
                            description = "Sends reminders focused on the long-term cost of inaction.",
                            checked = true
                        )
                        ToggleSettingItem(
                            title = "Opportunity Leak",
                            description = "Alerts you when your inactivity window hits critical levels.",
                            checked = false
                        )
                    }
                }
            }

            // Notifications Section
            item {
                SettingsSection(
                    title = "Notifications",
                    icon = Icons.Default.Notifications
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Quiet Mode Duration", color = Color.White, fontSize = 14.sp)
                                Text("2 Hours", color = EnafActionBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Slider(
                                value = 2f,
                                onValueChange = {},
                                valueRange = 0f..8f,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color.White,
                                    activeTrackColor = EnafActionBlue,
                                    inactiveTrackColor = EnafProgressBg
                                )
                            )
                        }
                        
                        ToggleSettingItem(title = "Smart Alerts", checked = true)
                        ToggleSettingItem(title = "Pulse Notifications", checked = true)
                    }
                }
            }

            // Appearance Section
            item {
                SettingsSection(
                    title = "Appearance",
                    icon = Icons.Default.Settings
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppearanceModeItem("Light", Icons.Default.Star, false, Modifier.weight(1f))
                        AppearanceModeItem("Dark", Icons.Default.Info, true, Modifier.weight(1f))
                        AppearanceModeItem("Celestial", Icons.Default.Face, false, Modifier.weight(1f))
                    }
                }
            }
            
            // Logout Button
            item {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EnafHeaderBg),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EnafHeaderBorder)
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
fun ProfileCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafCardBg, RoundedCornerShape(16.dp))
            .border(1.dp, EnafBorder, RoundedCornerShape(16.dp))
            .padding(25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Image Placeholder
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(EnafHeaderBg)
                .border(2.dp, EnafActionBlue, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color.Gray)
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column {
            Text(text = "Winzer Prince", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "aita.josh@example.com", color = EnafTextSecondary, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(EnafActionBlue.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(text = "PRO MEMBER", color = EnafActionBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
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
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun ToggleSettingItem(
    title: String,
    description: String? = null,
    checked: Boolean
) {
    var isChecked by remember { mutableStateOf(checked) }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            if (description != null) {
                Text(text = description, color = EnafTextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = EnafActionBlue
            )
        )
    }
}

@Composable
fun AppearanceModeItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(98.dp)
            .background(
                if (selected) EnafActionBlue.copy(alpha = 0.1f) else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                if (selected) EnafActionBlue else EnafHeaderBorder,
                RoundedCornerShape(12.dp)
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(if (selected) EnafActionBlue else EnafCardBg, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, color = Color.White, fontSize = 12.sp)
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    EnafTheme(darkTheme = true) {
        SettingsScreen()
    }
}
