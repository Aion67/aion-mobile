package com.example.enaf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enaf.data.local.entity.AchievementCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: AchievementCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entries: List<AchievementCacheEntity>)

    @Query("SELECT * FROM achievement_cache WHERE user_id = :userId ORDER BY unlocked_at DESC")
    fun observeByUser(userId: String): Flow<List<AchievementCacheEntity>>

    @Query("DELETE FROM achievement_cache WHERE user_id = :userId")
    suspend fun deleteByUser(userId: String)
}
