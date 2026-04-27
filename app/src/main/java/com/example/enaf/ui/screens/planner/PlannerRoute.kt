package com.example.enaf.ui.screens.planner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.enaf.data.local.EnafDatabaseProvider
import com.example.enaf.data.local.entity.AppLimitEntity
import com.example.enaf.data.local.entity.TrackedAppEntity
import com.example.enaf.data.local.entity.UsageSessionEntity
import com.example.enaf.data.local.entity.UserSessionEntity
import com.example.enaf.data.repository.LocalRepository
import com.example.enaf.data.repository.RoomLocalRepository

@Composable
fun PlannerRoute(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val localRepository = remember(context) {
        RoomLocalRepository(EnafDatabaseProvider.get(context))
    }
    val viewModel = remember(localRepository) { PlannerViewModel(localRepository) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(localRepository) {
        seedPlannerDataIfEmpty(localRepository)
    }

    PlannerScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}

private suspend fun seedPlannerDataIfEmpty(localRepository: LocalRepository) {
    val existingSession = localRepository.getActiveSession()
    if (existingSession != null) return

    val now = System.currentTimeMillis()
    val userId = "guest-local-user"
    localRepository.upsertSession(
        UserSessionEntity(
            id = "session-guest",
            userId = userId,
            authToken = null,
            onboardingSeen = true,
            createdAtEpochMillis = now,
        )
    )

    val trackedApps = listOf(
        TrackedAppEntity(
            id = "tracked-tiktok",
            userId = userId,
            packageName = "com.zhiliaoapp.musically",
            alias = "TikTok",
            category = "social",
            isBlocked = false,
            isIgnored = false,
            createdAtEpochMillis = now,
        ),
        TrackedAppEntity(
            id = "tracked-youtube",
            userId = userId,
            packageName = "com.google.android.youtube",
            alias = "YouTube",
            category = "video",
            isBlocked = false,
            isIgnored = false,
            createdAtEpochMillis = now,
        ),
        TrackedAppEntity(
            id = "tracked-instagram",
            userId = userId,
            packageName = "com.instagram.android",
            alias = "Instagram",
            category = "social",
            isBlocked = false,
            isIgnored = false,
            createdAtEpochMillis = now,
        ),
    )
    localRepository.upsertTrackedApps(trackedApps)

    localRepository.upsertAppLimits(
        listOf(
            AppLimitEntity(
                id = "limit-tiktok",
                trackedAppId = "tracked-tiktok",
                dailyLimitMinutes = 60,
                warningThresholdMinutes = 10,
                resetPolicy = "daily",
                isActive = true,
                updatedAtEpochMillis = now,
            ),
            AppLimitEntity(
                id = "limit-youtube",
                trackedAppId = "tracked-youtube",
                dailyLimitMinutes = 120,
                warningThresholdMinutes = 15,
                resetPolicy = "daily",
                isActive = true,
                updatedAtEpochMillis = now,
            ),
            AppLimitEntity(
                id = "limit-instagram",
                trackedAppId = "tracked-instagram",
                dailyLimitMinutes = 45,
                warningThresholdMinutes = 10,
                resetPolicy = "daily",
                isActive = true,
                updatedAtEpochMillis = now,
            ),
        )
    )

    localRepository.upsertUsageSessions(
        listOf(
            UsageSessionEntity(
                id = "usage-tiktok-1",
                trackedAppId = "tracked-tiktok",
                startedAtEpochMillis = now - 5_400_000L,
                endedAtEpochMillis = now - 2_700_000L,
                durationMinutes = 45,
                source = "system_usage_stats",
                wasInterrupted = false,
            ),
            UsageSessionEntity(
                id = "usage-youtube-1",
                trackedAppId = "tracked-youtube",
                startedAtEpochMillis = now - 9_600_000L,
                endedAtEpochMillis = now - 4_800_000L,
                durationMinutes = 80,
                source = "system_usage_stats",
                wasInterrupted = false,
            ),
            UsageSessionEntity(
                id = "usage-instagram-1",
                trackedAppId = "tracked-instagram",
                startedAtEpochMillis = now - 2_040_000L,
                endedAtEpochMillis = now - 1_320_000L,
                durationMinutes = 12,
                source = "system_usage_stats",
                wasInterrupted = false,
            ),
        )
    )
}
