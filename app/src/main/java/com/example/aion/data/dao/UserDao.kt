package com.example.aion.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aion.data.entities.UserPreferenceEntity
import com.example.aion.data.entities.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profiles LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM user_preferences")
    fun getAllPreferences(): Flow<List<UserPreferenceEntity>>

    @Query("SELECT * FROM user_preferences WHERE `key` = :key")
    fun getPreference(key: String): Flow<UserPreferenceEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePreference(preference: UserPreferenceEntity)
}
