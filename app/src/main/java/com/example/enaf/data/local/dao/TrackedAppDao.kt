package com.example.enaf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enaf.data.local.entity.TrackedAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackedAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(app: TrackedAppEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(apps: List<TrackedAppEntity>)

    @Query("SELECT * FROM tracked_app WHERE user_id = :userId ORDER BY alias ASC")
    fun observeByUser(userId: String): Flow<List<TrackedAppEntity>>

    @Query("SELECT * FROM tracked_app WHERE user_id = :userId ORDER BY alias ASC")
    suspend fun getByUser(userId: String): List<TrackedAppEntity>

    @Query("DELETE FROM tracked_app WHERE id = :id")
    suspend fun deleteById(id: String)
}
