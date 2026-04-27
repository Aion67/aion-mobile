package com.example.enaf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enaf.data.local.entity.AppLimitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppLimitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(limit: AppLimitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(limits: List<AppLimitEntity>)

    @Query("SELECT * FROM app_limit WHERE tracked_app_id = :trackedAppId ORDER BY updated_at DESC")
    fun observeByTrackedApp(trackedAppId: String): Flow<List<AppLimitEntity>>

    @Query("DELETE FROM app_limit WHERE tracked_app_id = :trackedAppId")
    suspend fun deleteByTrackedApp(trackedAppId: String)
}
