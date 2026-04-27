package com.example.enaf.data.seed

import com.example.enaf.data.local.entity.AchievementCacheEntity
import com.example.enaf.data.local.entity.AppLimitEntity
import com.example.enaf.data.local.entity.DailySummaryEntity
import com.example.enaf.data.local.entity.TrackedAppEntity
import com.example.enaf.data.local.entity.UsageSessionEntity
import com.example.enaf.data.local.entity.UserSessionEntity
import com.example.enaf.data.local.entity.WalletLedgerEntity
import com.example.enaf.data.repository.LocalRepository

suspend fun ensureLocalSeedData(localRepository: LocalRepository) {
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

    localRepository.upsertTrackedApps(
        listOf(
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
    )

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

    localRepository.upsertWalletLedger(
        WalletLedgerEntity(
            id = "wallet-guest",
            userId = userId,
            coinsBalanceCache = 1240,
            diamondsBalanceCache = 36,
            updatedAtEpochMillis = now,
        )
    )

    localRepository.upsertDailySummaries(
        listOf(
            DailySummaryEntity(
                id = "summary-day-1",
                userId = userId,
                summaryDate = "2026-04-25",
                focusMinutes = 210,
                scrollMinutes = 86,
                timeSavedMinutes = 34,
                completionRate = 0.62f,
                creditScore = 720,
                streakDay = 8,
            ),
            DailySummaryEntity(
                id = "summary-day-2",
                userId = userId,
                summaryDate = "2026-04-26",
                focusMinutes = 244,
                scrollMinutes = 73,
                timeSavedMinutes = 42,
                completionRate = 0.69f,
                creditScore = 760,
                streakDay = 9,
            ),
            DailySummaryEntity(
                id = "summary-day-3",
                userId = userId,
                summaryDate = "2026-04-27",
                focusMinutes = 268,
                scrollMinutes = 66,
                timeSavedMinutes = 51,
                completionRate = 0.74f,
                creditScore = 804,
                streakDay = 10,
            ),
        )
    )

    localRepository.upsertAchievementCache(
        listOf(
            AchievementCacheEntity(
                id = "ach-1",
                userId = userId,
                achievementId = "3-day-blackout",
                unlockedAtEpochMillis = now - 172_800_000L,
            ),
            AchievementCacheEntity(
                id = "ach-2",
                userId = userId,
                achievementId = "night-discipline",
                unlockedAtEpochMillis = now - 86_400_000L,
            ),
            AchievementCacheEntity(
                id = "ach-3",
                userId = userId,
                achievementId = "veteran-streak",
                unlockedAtEpochMillis = now - 43_200_000L,
            ),
        )
    )
}
