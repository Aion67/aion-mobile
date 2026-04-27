package com.example.enaf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enaf.data.local.entity.UsageSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(session: UsageSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(sessions: List<UsageSessionEntity>)

    @Query(
        "SELECT * FROM usage_session WHERE tracked_app_id = :trackedAppId ORDER BY started_at DESC"
    )
    fun observeByTrackedApp(trackedAppId: String): Flow<List<UsageSessionEntity>>

    @Query(
        "SELECT * FROM usage_session WHERE tracked_app_id = :trackedAppId AND started_at BETWEEN :start AND :end ORDER BY started_at ASC"
    )
    suspend fun getByTrackedAppAndRange(
        trackedAppId: String,
        start: Long,
        end: Long,
    ): List<UsageSessionEntity>

    @Query("DELETE FROM usage_session WHERE tracked_app_id = :trackedAppId")
    suspend fun deleteByTrackedApp(trackedAppId: String)
}
