package com.example.aion.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aion.data.entities.AchievementEntity
import com.example.aion.data.entities.PointsTransactionEntity
import com.example.aion.data.entities.UserAchievementEntity
import com.example.aion.data.entities.UserPointsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GamificationDao {
    @Query("SELECT * FROM user_points WHERE userId = :userId")
    fun getUserPoints(userId: String = "default_user"): Flow<UserPointsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePoints(points: UserPointsEntity)

    @Query("SELECT * FROM points_transactions ORDER BY timestamp DESC")
    fun getPointsTransactions(): Flow<List<PointsTransactionEntity>>

    @Insert
    suspend fun insertTransaction(transaction: PointsTransactionEntity)

    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Query("SELECT * FROM user_achievements")
    fun getUserAchievements(): Flow<List<UserAchievementEntity>>

    @Insert
    suspend fun earnAchievement(userAchievement: UserAchievementEntity)
}
