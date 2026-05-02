package com.example.aion.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aion.data.entities.TrackedAppEntity
import com.example.aion.data.entities.AppSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM tracked_apps")
    fun getAllTrackedApps(): Flow<List<TrackedAppEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackedApp(app: TrackedAppEntity)

    @Query("SELECT * FROM app_settings WHERE appPackageName = :packageName")
    fun getSettingsForApp(packageName: String): Flow<AppSettingsEntity?>

    @Update
    suspend fun updateSettings(settings: AppSettingsEntity)

    @Query("DELETE FROM tracked_apps WHERE packageName = :packageName")
    suspend fun deleteTrackedApp(packageName: String)
}
