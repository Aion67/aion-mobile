package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.components.AionSettingsRow
import com.example.aion.ui.components.AionSettingsSection
import com.example.aion.ui.components.AionTopAppBar
import com.example.aion.ui.components.ProfileHeaderCard
import com.example.aion.ui.components.ProfilePictureCard
import com.example.aion.ui.components.ProfileUsernameCard
import com.example.aion.ui.theme.Variables

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aion.ui.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var editUsername by remember { mutableStateOf("") }

    LaunchedEffect(uiState.profile.username) {
        editUsername = uiState.profile.username
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
        containerColor = Variables.SchemesSurface,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Variables.SchemesSurface)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ProfileHeaderCard(
                displayName = uiState.profile.username,
                username = uiState.profile.username,
                avatarRes = com.example.aion.R.drawable.tiktok, // Placeholder
                bio = "Aion user focusing on time.",
            )

            AionSettingsSection(title = "Profile Picture") {
                ProfilePictureCard(
                    avatarRes = com.example.aion.R.drawable.tiktok,
                    onChangePictureClick = { /* Implement file picker later */ },
                )
            }

            AionSettingsSection(title = "Username") {
                ProfileUsernameCard(
                    username = editUsername,
                    onUsernameChange = { editUsername = it },
                    onSaveUsername = { viewModel.updateUsername(editUsername) },
                )
            }

            AionSettingsSection(title = "Account") {
                AionSettingsRow(
                    title = "Display name",
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