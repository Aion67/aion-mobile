package com.example.enaf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_session",
    indices = [Index(value = ["user_id"], unique = true)]
)
data class UserSessionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "auth_token")
    val authToken: String?,
    @ColumnInfo(name = "onboarding_seen")
    val onboardingSeen: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAtEpochMillis: Long,
)
