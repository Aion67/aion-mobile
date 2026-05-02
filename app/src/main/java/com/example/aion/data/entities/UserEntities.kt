package com.example.aion.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Stores user profile information.
 */
@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val username: String,
    val avatarUri: String? = null,
    val createdDate: Long = System.currentTimeMillis()
)

/**
 * Stores user-wide preferences (Theme, Accent, etc.).
 */
@Entity(tableName = "user_preferences")
data class UserPreferenceEntity(
    @PrimaryKey val key: String, // e.g., "theme_mode", "accent_color"
    val value: String
)
