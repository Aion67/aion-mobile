package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.components.AionTopAppBar
import com.example.aion.ui.theme.Variables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun SettingsDetailScreen(
    title: String,
    body: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(title = title, leadingIcon = Icons.AutoMirrored.Filled.ArrowBack, onLeadingClick = onBack)
        },
        containerColor = Variables.SchemesSurface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Variables.SchemesSurface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = Variables.StaticBodyLargeSize,
                    lineHeight = Variables.StaticBodyLargeLineHeight,
                    color = Variables.SchemesOnSurface,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsDetailScreenPreview() {
    SettingsDetailScreen(
        title = "FAQ",
        body = "Frequently asked questions go here.",
        onBack = {}
    )
}
