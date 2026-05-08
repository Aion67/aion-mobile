package com.example.aion.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val avatarPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            try {
                context.contentResolver.takePersistableUriPermission(uri, flags)
            } catch (_: SecurityException) {
                // Fallback: still save the URI even if the provider does not allow persistence.
            }
            viewModel.updateAvatar(uri.toString())
        }
    }

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
                avatarRes = com.example.aion.R.drawable.tiktok, // Placeholder resource
                avatarUri = uiState.profile.avatarUri,
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
                    avatarUri = uiState.profile.avatarUri,
                    onChangePictureClick = { avatarPickerLauncher.launch(arrayOf("image/*")) },
                )
            }

            AionSettingsSection(title = "Display Name") {
                val isNameChanged = editDisplayName.trim().isNotEmpty() && editDisplayName != uiState.profile.displayName

                ProfileUsernameCard(
                    username = editDisplayName,
                    onUsernameChange = { editDisplayName = it },
                    onSaveUsername = { viewModel.updateDisplayName(editDisplayName) },
                    isSaveEnabled = isNameChanged
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
