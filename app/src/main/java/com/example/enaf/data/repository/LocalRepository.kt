package com.example.enaf.data.repository

import com.example.enaf.data.local.entity.AchievementCacheEntity
import com.example.enaf.data.local.entity.AppLimitEntity
import com.example.enaf.data.local.entity.DailySummaryEntity
import com.example.enaf.data.local.entity.SettingsEntity
import com.example.enaf.data.local.entity.TrackedAppEntity
import com.example.enaf.data.local.entity.UsageSessionEntity
import com.example.enaf.data.local.entity.UserSessionEntity
import com.example.enaf.data.local.entity.WalletLedgerEntity
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    fun observeActiveSession(): Flow<UserSessionEntity?>
    suspend fun getActiveSession(): UserSessionEntity?
    suspend fun upsertSession(session: UserSessionEntity)
    suspend fun clearSession()

    fun observeTrackedApps(userId: String): Flow<List<TrackedAppEntity>>
    suspend fun getTrackedApps(userId: String): List<TrackedAppEntity>
    suspend fun upsertTrackedApp(app: TrackedAppEntity)
    suspend fun upsertTrackedApps(apps: List<TrackedAppEntity>)
    suspend fun deleteTrackedApp(id: String)

    fun observeAppLimits(trackedAppId: String): Flow<List<AppLimitEntity>>
    suspend fun upsertAppLimit(limit: AppLimitEntity)
    suspend fun upsertAppLimits(limits: List<AppLimitEntity>)
    suspend fun deleteAppLimits(trackedAppId: String)

    fun observeUsageSessions(trackedAppId: String): Flow<List<UsageSessionEntity>>
    suspend fun getUsageSessionsInRange(
        trackedAppId: String,
        start: Long,
        end: Long,
    ): List<UsageSessionEntity>
    suspend fun upsertUsageSession(session: UsageSessionEntity)
    suspend fun upsertUsageSessions(sessions: List<UsageSessionEntity>)
    suspend fun deleteUsageSessions(trackedAppId: String)

    fun observeDailySummaries(userId: String): Flow<List<DailySummaryEntity>>
    suspend fun getDailySummaries(userId: String): List<DailySummaryEntity>
    suspend fun upsertDailySummary(summary: DailySummaryEntity)
    suspend fun upsertDailySummaries(summaries: List<DailySummaryEntity>)
    suspend fun deleteDailySummaries(userId: String)

    fun observeSettings(userId: String): Flow<SettingsEntity?>
    suspend fun getSettings(userId: String): SettingsEntity?
    suspend fun upsertSettings(settings: SettingsEntity)

    fun observeAchievementCache(userId: String): Flow<List<AchievementCacheEntity>>
    suspend fun upsertAchievementCache(entry: AchievementCacheEntity)
    suspend fun upsertAchievementCache(entries: List<AchievementCacheEntity>)
    suspend fun clearAchievementCache(userId: String)

    fun observeWalletLedger(userId: String): Flow<WalletLedgerEntity?>
    suspend fun getWalletLedger(userId: String): WalletLedgerEntity?
    suspend fun upsertWalletLedger(entry: WalletLedgerEntity)
}
