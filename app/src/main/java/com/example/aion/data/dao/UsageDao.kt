package com.example.aion.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.aion.data.entities.UsageSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageDao {
    @Query("SELECT * FROM usage_sessions WHERE appPackageName = :packageName")
    fun getSessionsForApp(packageName: String): Flow<List<UsageSessionEntity>>

    @Insert
    suspend fun insertSession(session: UsageSessionEntity)

    @Query("SELECT SUM(totalDurationMs) FROM usage_sessions WHERE appPackageName = :packageName AND startTime >= :since")
    fun getTotalDurationForApp(packageName: String, since: Long): Flow<Long?>
}
