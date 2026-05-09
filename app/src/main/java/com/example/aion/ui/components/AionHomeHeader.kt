package com.example.aion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.R
import com.example.aion.ui.theme.Variables

/**
 * Header for Home screen showing avatar, greeting and a small summary row.
 */
@Composable
fun AionHomeHeader(
    userName: String,
    improvementPercentage: Float,
    avatarRes: Int? = null,
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
        blurRadius = 24.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Welcome back,",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = Variables.StaticLabelLargeSize,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Normal
                    )
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = Variables.StaticHeadlineSmallSize,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                )

                val improvementText = if (improvementPercentage >= 0) {
                    "+${(improvementPercentage * 100).toInt()}% Improvement"
                } else {
                    "${(improvementPercentage * 100).toInt()}% Improvement"
                }
                val improvementColor = if (improvementPercentage >= 0) {
                    Variables.SuccessGreen
                } else {
                    Variables.WarningRed
                }

                Text(
                    text = improvementText,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = Variables.StaticLabelMediumSize,
                        color = improvementColor,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            IconButton(
                onClick = onAvatarClick,
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
            ) {
                if (avatarRes != null) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AionHomeHeaderPreview() {
    AionHomeHeader(userName = "Alex", improvementPercentage = 0.05f)
}
