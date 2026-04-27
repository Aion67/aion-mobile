package com.example.enaf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enaf.data.local.entity.DailySummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailySummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(summary: DailySummaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(summaries: List<DailySummaryEntity>)

    @Query("SELECT * FROM daily_summary WHERE user_id = :userId ORDER BY summary_date DESC")
    fun observeByUser(userId: String): Flow<List<DailySummaryEntity>>

    @Query("SELECT * FROM daily_summary WHERE user_id = :userId ORDER BY summary_date DESC")
    suspend fun getByUser(userId: String): List<DailySummaryEntity>

    @Query("DELETE FROM daily_summary WHERE user_id = :userId")
    suspend fun deleteByUser(userId: String)
}
