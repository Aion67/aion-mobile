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
 * A search bar component for Aion.
 * Matches Figma node 4:784.
 */
@Composable
fun AionSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search apps",
    onClear: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = CircleShape,
        color = Variables.SchemesSurfaceContainerHigh
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
                    .padding(start = 12.dp)
                    .size(24.dp),
                tint = Variables.SchemesOnSurfaceVariant
            )

            // Search TextField (BasicTextField or TextField without decoration)
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
                            lineHeight = Variables.StaticBodyLargeLineHeight,
                            letterSpacing = Variables.StaticBodyLargeTracking,
                            color = Variables.SchemesOnSurfaceVariant
                        )
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Variables.PrimaryBrand
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = Variables.StaticBodyLargeSize,
                    color = Variables.SchemesOnSurface
                )
            )

            // Trailing Icon (Clear button)
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        modifier = Modifier.size(24.dp),
                        tint = Variables.SchemesOnSurfaceVariant
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
        Spacer(Modifier.height(16.dp))
        AionSearchBar(
            query = "Insta",
            onQueryChange = {},
            onClear = {}
        )
    }
}
