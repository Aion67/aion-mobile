package com.example.aion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import coil.compose.AsyncImage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables

@Composable
fun LetterAvatar(
    name: String,
    modifier: Modifier = Modifier,
    backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onPrimary
) {
    val initial = name.firstOrNull()?.uppercase() ?: "?"
    androidx.compose.foundation.layout.Box(
        modifier = modifier.background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            color = textColor,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfileHeaderCard(
    displayName: String,
    username: String,
    avatarUri: String?,
    bio: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (!avatarUri.isNullOrBlank() && !avatarUri.startsWith("res:")) {
                AsyncImage(
                    model = avatarUri,
                    contentDescription = displayName,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            } else {
                LetterAvatar(
                    name = displayName.ifBlank { username },
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = Variables.StaticTitleLargeSize,
                        lineHeight = Variables.StaticTitleLargeLineHeight,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                )
                Text(
                    text = username,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = Variables.StaticBodyMediumSize,
                        lineHeight = Variables.StaticBodyMediumLineHeight,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
                Text(
                    text = bio,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = Variables.StaticBodySmallSize,
                        lineHeight = Variables.StaticBodySmallLineHeight,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
            }
        }
    }
}

@Composable
fun ProfilePictureCard(
    displayName: String,
    avatarUri: String?,
    onChangePictureClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!avatarUri.isNullOrBlank() && !avatarUri.startsWith("res:")) {
                AsyncImage(
                    model = avatarUri,
                    contentDescription = "Profile picture preview",
                    modifier = Modifier
                        .size(112.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop,
                )
            } else {
                LetterAvatar(
                    name = displayName,
                    modifier = Modifier
                        .size(112.dp)
                        .clip(CircleShape)
                )
            }

            Button(onClick = onChangePictureClick) {
                Text(text = "Change profile picture")
            }
        }
    }
}

@Composable
fun ProfileUsernameCard(
    username: String,
    onUsernameChange: (String) -> Unit,
    onSaveUsername: () -> Unit,
    isSaveEnabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Display Name",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = Variables.StaticTitleMediumSize,
                    lineHeight = Variables.StaticTitleMediumLineHeight,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            )

            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Enter display name") },
            )

            Button(onClick = onSaveUsername, enabled = isSaveEnabled) {
                Text(text = "Save display name")
            }
        }
    }
}
