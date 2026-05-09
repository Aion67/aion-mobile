package com.example.aion.data.repository

import com.example.aion.data.dao.UsageDao
import com.example.aion.data.entities.UsageSessionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface UsageRepository {
    fun getSessionsForApp(packageName: String): Flow<List<UsageSessionEntity>>
    fun getAllSessions(): Flow<List<UsageSessionEntity>>
    suspend fun logUsageSession(session: UsageSessionEntity)
    fun getTotalUsageForApp(packageName: String, since: Long): Flow<Long?>
    fun getTotalUsageForAppInRange(packageName: String, start: Long, end: Long): Flow<Long?>
    suspend fun resetTodayUsage(packageName: String, todayStart: Long)
}

@Singleton
class UsageRepositoryImpl @Inject constructor(
    private val usageDao: UsageDao
) : UsageRepository {
    override fun getSessionsForApp(packageName: String) = usageDao.getSessionsForApp(packageName)

    override fun getAllSessions() = usageDao.getAllSessions()

    override suspend fun logUsageSession(session: UsageSessionEntity) {
        usageDao.insertSession(session)
    }

    override fun getTotalUsageForApp(packageName: String, since: Long) = 
        usageDao.getTotalDurationForApp(packageName, since)

    override fun getTotalUsageForAppInRange(packageName: String, start: Long, end: Long) =
        usageDao.getTotalDurationForAppInRange(packageName, start, end)

    override suspend fun resetTodayUsage(packageName: String, todayStart: Long) {
        usageDao.deleteSessionsForAppSince(packageName, todayStart)
    }
}
