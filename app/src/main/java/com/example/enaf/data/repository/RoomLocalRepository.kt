package com.example.enaf.data.repository

import com.example.enaf.data.local.EnafDatabase
import com.example.enaf.data.local.entity.AchievementCacheEntity
import com.example.enaf.data.local.entity.AppLimitEntity
import com.example.enaf.data.local.entity.DailySummaryEntity
import com.example.enaf.data.local.entity.SettingsEntity
import com.example.enaf.data.local.entity.TrackedAppEntity
import com.example.enaf.data.local.entity.UsageSessionEntity
import com.example.enaf.data.local.entity.UserSessionEntity
import com.example.enaf.data.local.entity.WalletLedgerEntity
import kotlinx.coroutines.flow.Flow

class RoomLocalRepository(
    private val database: EnafDatabase,
) : LocalRepository {

    override fun observeActiveSession(): Flow<UserSessionEntity?> {
        return database.userSessionDao().observeActiveSession()
    }

    override suspend fun getActiveSession(): UserSessionEntity? {
        return database.userSessionDao().getActiveSession()
    }

    override suspend fun upsertSession(session: UserSessionEntity) {
        database.userSessionDao().upsert(session)
    }

    override suspend fun clearSession() {
        database.userSessionDao().clear()
    }

    override fun observeTrackedApps(userId: String): Flow<List<TrackedAppEntity>> {
        return database.trackedAppDao().observeByUser(userId)
    }

    override suspend fun getTrackedApps(userId: String): List<TrackedAppEntity> {
        return database.trackedAppDao().getByUser(userId)
    }

    override suspend fun upsertTrackedApp(app: TrackedAppEntity) {
        database.trackedAppDao().upsert(app)
    }

    override suspend fun upsertTrackedApps(apps: List<TrackedAppEntity>) {
        database.trackedAppDao().upsertAll(apps)
    }

    override suspend fun deleteTrackedApp(id: String) {
        database.trackedAppDao().deleteById(id)
    }

    override fun observeAppLimits(trackedAppId: String): Flow<List<AppLimitEntity>> {
        return database.appLimitDao().observeByTrackedApp(trackedAppId)
    }

    override suspend fun getAppLimits(trackedAppId: String): List<AppLimitEntity> {
        return database.appLimitDao().getByTrackedApp(trackedAppId)
    }

    override suspend fun upsertAppLimit(limit: AppLimitEntity) {
        database.appLimitDao().upsert(limit)
    }

    override suspend fun upsertAppLimits(limits: List<AppLimitEntity>) {
        database.appLimitDao().upsertAll(limits)
    }

    override suspend fun deleteAppLimits(trackedAppId: String) {
        database.appLimitDao().deleteByTrackedApp(trackedAppId)
    }

    override fun observeUsageSessions(trackedAppId: String): Flow<List<UsageSessionEntity>> {
        return database.usageSessionDao().observeByTrackedApp(trackedAppId)
    }

    override suspend fun getUsageSessionsInRange(
        trackedAppId: String,
        start: Long,
        end: Long,
    ): List<UsageSessionEntity> {
        return database.usageSessionDao().getByTrackedAppAndRange(trackedAppId, start, end)
    }

    override suspend fun upsertUsageSession(session: UsageSessionEntity) {
        database.usageSessionDao().upsert(session)
    }

    override suspend fun upsertUsageSessions(sessions: List<UsageSessionEntity>) {
        database.usageSessionDao().upsertAll(sessions)
    }

    override suspend fun deleteUsageSessions(trackedAppId: String) {
        database.usageSessionDao().deleteByTrackedApp(trackedAppId)
    }

    override fun observeDailySummaries(userId: String): Flow<List<DailySummaryEntity>> {
        return database.dailySummaryDao().observeByUser(userId)
    }

    override suspend fun getDailySummaries(userId: String): List<DailySummaryEntity> {
        return database.dailySummaryDao().getByUser(userId)
    }

    override suspend fun upsertDailySummary(summary: DailySummaryEntity) {
        database.dailySummaryDao().upsert(summary)
    }

    override suspend fun upsertDailySummaries(summaries: List<DailySummaryEntity>) {
        database.dailySummaryDao().upsertAll(summaries)
    }

    override suspend fun deleteDailySummaries(userId: String) {
        database.dailySummaryDao().deleteByUser(userId)
    }

    override fun observeSettings(userId: String): Flow<SettingsEntity?> {
        return database.settingsDao().observeByUser(userId)
    }

    override suspend fun getSettings(userId: String): SettingsEntity? {
        return database.settingsDao().getByUser(userId)
    }

    override suspend fun upsertSettings(settings: SettingsEntity) {
        database.settingsDao().upsert(settings)
    }

    override fun observeAchievementCache(userId: String): Flow<List<AchievementCacheEntity>> {
        return database.achievementCacheDao().observeByUser(userId)
    }

    override suspend fun getAchievementCache(userId: String): List<AchievementCacheEntity> {
        return database.achievementCacheDao().getByUser(userId)
    }

    override suspend fun upsertAchievementCache(entry: AchievementCacheEntity) {
        database.achievementCacheDao().upsert(entry)
    }

    override suspend fun upsertAchievementCache(entries: List<AchievementCacheEntity>) {
        database.achievementCacheDao().upsertAll(entries)
    }

    override suspend fun clearAchievementCache(userId: String) {
        database.achievementCacheDao().deleteByUser(userId)
    }

    override fun observeWalletLedger(userId: String): Flow<WalletLedgerEntity?> {
        return database.walletLedgerDao().observeByUser(userId)
    }

    override suspend fun getWalletLedger(userId: String): WalletLedgerEntity? {
        return database.walletLedgerDao().getByUser(userId)
    }

    override suspend fun upsertWalletLedger(entry: WalletLedgerEntity) {
        database.walletLedgerDao().upsert(entry)
    }
}
