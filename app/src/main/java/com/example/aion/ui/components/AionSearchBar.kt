package com.example.aion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables

/**
 * Revamped Search Bar with glassmorphism and fixed text visibility.
 */
@Composable
fun AionSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search apps",
    onClear: () -> Unit = {}
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
        blurRadius = 12.dp,
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading Icon
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Search TextField
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = Variables.StaticBodyLargeSize,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = Variables.StaticBodyLargeSize,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )

            // Trailing Icon (Clear button)
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AionSearchBarPreview() {
    Column(Modifier.padding(16.dp)) {
        AionSearchBar(
            query = "",
            onQueryChange = {}
        )
    }
}
