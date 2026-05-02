package com.example.aion.data.repository

import com.example.aion.data.dao.GamificationDao
import com.example.aion.data.entities.AchievementEntity
import com.example.aion.data.entities.PointsTransactionEntity
import com.example.aion.data.entities.UserAchievementEntity
import com.example.aion.data.entities.UserPointsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface GamificationRepository {
    fun getUserPoints(userId: String = "default_user"): Flow<UserPointsEntity?>
    suspend fun updatePoints(points: UserPointsEntity)
    fun getPointsTransactions(): Flow<List<PointsTransactionEntity>>
    suspend fun addTransaction(transaction: PointsTransactionEntity)
    fun getAllAchievements(): Flow<List<AchievementEntity>>
    fun getUserAchievements(): Flow<List<UserAchievementEntity>>
    suspend fun earnAchievement(userAchievement: UserAchievementEntity)
}

@Singleton
class GamificationRepositoryImpl @Inject constructor(
    private val gamificationDao: GamificationDao
) : GamificationRepository {
    override fun getUserPoints(userId: String) = gamificationDao.getUserPoints(userId)

    override suspend fun updatePoints(points: UserPointsEntity) {
        gamificationDao.updatePoints(points)
    }

    override fun getPointsTransactions() = gamificationDao.getPointsTransactions()

    override suspend fun addTransaction(transaction: PointsTransactionEntity) {
        gamificationDao.insertTransaction(transaction)
    }

    override fun getAllAchievements() = gamificationDao.getAllAchievements()

    override fun getUserAchievements() = gamificationDao.getUserAchievements()

    override suspend fun earnAchievement(userAchievement: UserAchievementEntity) {
        gamificationDao.earnAchievement(userAchievement)
    }
}
