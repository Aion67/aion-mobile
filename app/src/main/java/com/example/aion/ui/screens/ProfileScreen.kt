package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.components.*
import com.example.aion.ui.theme.Variables
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.aion.ui.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var editDisplayName by remember { mutableStateOf("") }

    LaunchedEffect(uiState.profile.displayName) {
        editDisplayName = uiState.profile.displayName
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "Profile",
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onLeadingClick = onBack,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ProfileHeaderCard(
                displayName = uiState.profile.displayName,
                username = uiState.profile.username,
                avatarRes = com.example.aion.R.drawable.tiktok, // Placeholder
                bio = "Aion user focusing on productivity.",
            )

            AionSettingsSection(title = "Stats") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AionStatCard(
                        percentage = 0, // Not used here
                        label = "Time Saved",
                        modifier = Modifier.weight(1f),
                        progressColor = Variables.SuccessGreen
                    )
                    // Customizing AionStatCard to show text instead of percentage might be needed
                }
            }

            AionSettingsSection(title = "Profile Picture") {
                ProfilePictureCard(
                    avatarRes = com.example.aion.R.drawable.tiktok,
                    onChangePictureClick = { /* Implement file picker later */ },
                )
            }

            AionSettingsSection(title = "Display Name") {
                ProfileUsernameCard(
                    username = editDisplayName,
                    onUsernameChange = { editDisplayName = it },
                    onSaveUsername = { viewModel.updateDisplayName(editDisplayName) },
                )
            }

            AionSettingsSection(title = "Account Information") {
                AionSettingsRow(
                    title = "System Username",
                    subtitle = uiState.profile.username,
                    leadingIcon = Icons.Filled.Person,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(onBack = {})
}
