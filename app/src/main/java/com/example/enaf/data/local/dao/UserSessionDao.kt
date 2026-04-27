package com.example.enaf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enaf.data.local.entity.UserSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(session: UserSessionEntity)

    @Query("SELECT * FROM user_session LIMIT 1")
    fun observeActiveSession(): Flow<UserSessionEntity?>

    @Query("SELECT * FROM user_session LIMIT 1")
    suspend fun getActiveSession(): UserSessionEntity?

    @Query("DELETE FROM user_session")
    suspend fun clear()
}
