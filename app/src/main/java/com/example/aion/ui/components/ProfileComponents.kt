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
fun ProfileHeaderCard(
    displayName: String,
    username: String,
    avatarRes: Int,
    bio: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Variables.SchemesSurface),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = displayName,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = Variables.StaticTitleLargeSize,
                        lineHeight = Variables.StaticTitleLargeLineHeight,
                        fontWeight = FontWeight.Medium,
                        color = Variables.SchemesOnSurface,
                    )
                )
                Text(
                    text = username,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = Variables.StaticBodyMediumSize,
                        lineHeight = Variables.StaticBodyMediumLineHeight,
                        color = Variables.SchemesOnSurfaceVariant,
                    )
                )
                Text(
                    text = bio,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = Variables.StaticBodySmallSize,
                        lineHeight = Variables.StaticBodySmallLineHeight,
                        color = Variables.SchemesOnSurfaceVariant,
                    )
                )
            }
        }
    }
}

@Composable
fun ProfilePictureCard(
    avatarRes: Int,
    onChangePictureClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Variables.SchemesSurface),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = "Profile picture preview",
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(Variables.SchemesSurfaceContainerHigh),
                contentScale = ContentScale.Crop,
            )

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
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Variables.SchemesSurface),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Username",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = Variables.StaticTitleMediumSize,
                    lineHeight = Variables.StaticTitleMediumLineHeight,
                    fontWeight = FontWeight.Medium,
                    color = Variables.SchemesOnSurface,
                )
            )

            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Enter username") },
            )

            Button(onClick = onSaveUsername) {
                Text(text = "Save username")
            }
        }
    }
}