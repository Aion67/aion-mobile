package com.example.aion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.aion.R
import com.example.aion.ui.components.AionFilledButton
import com.example.aion.ui.theme.Variables
import com.example.aion.ui.viewmodels.OnboardingUiState
import com.example.aion.ui.viewmodels.OnboardingViewModel
import com.example.aion.utils.PermissionUtils
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    OnboardingContent(
        uiState = uiState,
        onFinish = {
            viewModel.completeOnboarding()
            onFinish()
        },
        onNameChange = { viewModel.updateDisplayName(it) },
        onPermissionUpdate = { context ->
            viewModel.updatePermissionStatus(
                usage = PermissionUtils.hasUsageStatsPermission(context),
                overlay = PermissionUtils.hasOverlayPermission(context)
            )
        }
    )
}

@Composable
fun OnboardingContent(
    uiState: OnboardingUiState,
    onFinish: () -> Unit,
    onNameChange: (String) -> Unit,
    onPermissionUpdate: (android.content.Context) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Update permission status when returning to screen
    LaunchedEffect(Unit) {
        onPermissionUpdate(context)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = true
            ) { page ->
                when (page) {
                    0 -> WelcomePage()
                    1 -> PermissionsPage(
                        usageGranted = uiState.usagePermissionGranted,
                        overlayGranted = uiState.overlayPermissionGranted,
                        onGrantUsage = { context.startActivity(PermissionUtils.getUsageStatsIntent()) },
                        onGrantOverlay = { context.startActivity(PermissionUtils.getOverlayIntent(context)) }
                    )
                    2 -> ProfilePage(
                        displayName = uiState.displayName,
                        onNameChange = onNameChange
                    )
                }
            }

            // Navigation Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Page Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) { i ->
                        val color = if (pagerState.currentPage == i) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        }
                        Surface(
                            modifier = Modifier.size(8.dp),
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = color
                        ) {}
                    }
                }

                AionFilledButton(
                    text = if (pagerState.currentPage == 2) "Get Started" else "Next",
                    onClick = {
                        if (pagerState.currentPage < 2) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinish()
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    com.example.aion.ui.theme.AionTheme {
        OnboardingContent(
            uiState = OnboardingUiState(displayName = "John Doe"),
            onFinish = {},
            onNameChange = {},
            onPermissionUpdate = {}
        )
    }
}

@Composable
private fun WelcomePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Welcome to Aion",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Master your time. Reduce distractions. Achieve your goals with smart app tracking.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PermissionsPage(
    usageGranted: Boolean,
    overlayGranted: Boolean,
    onGrantUsage: () -> Unit,
    onGrantOverlay: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Essential Permissions",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Aion needs these to work effectively.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        PermissionItem(
            icon = Icons.Default.Assessment,
            title = "Usage Access",
            description = "Needed to track how much time you spend on apps.",
            isGranted = usageGranted,
            onGrant = onGrantUsage
        )

        Spacer(modifier = Modifier.height(24.dp))

        PermissionItem(
            icon = Icons.Default.Layers,
            title = "Display Over Apps",
            description = "Needed to show alerts when you reach your limits.",
            isGranted = overlayGranted,
            onGrant = onGrantOverlay
        )
    }
}

@Composable
private fun PermissionItem(
    icon: ImageVector,
    title: String,
    description: String,
    isGranted: Boolean,
    onGrant: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.width(8.dp))
        if (isGranted) {
            Icon(
                imageVector = Icons.Default.RocketLaunch, // Using as "done" placeholder or similar
                contentDescription = "Granted",
                tint = Variables.SuccessGreen
            )
        } else {
            TextButton(onClick = onGrant) {
                Text("Grant")
            }
        }
    }
}

@Composable
private fun ProfilePage(
    displayName: String,
    onNameChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Almost There",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "What should we call you?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = displayName,
            onValueChange = onNameChange,
            label = { Text("Display Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )
    }
}
