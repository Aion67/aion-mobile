package com.example.enaf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enaf.data.local.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(settings: SettingsEntity)

    @Query("SELECT * FROM settings WHERE user_id = :userId LIMIT 1")
    fun observeByUser(userId: String): Flow<SettingsEntity?>

    @Query("SELECT * FROM settings WHERE user_id = :userId LIMIT 1")
    suspend fun getByUser(userId: String): SettingsEntity?
}
