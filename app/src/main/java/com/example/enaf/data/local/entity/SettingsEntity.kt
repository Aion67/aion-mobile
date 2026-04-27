package com.example.enaf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "settings",
    foreignKeys = [
        ForeignKey(
            entity = UserSessionEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(value = ["user_id"], unique = true)]
)
data class SettingsEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "allow_notifications")
    val allowNotifications: Boolean,
    @ColumnInfo(name = "allow_display_over_other_apps")
    val allowDisplayOverOtherApps: Boolean,
    @ColumnInfo(name = "theme_mode")
    val themeMode: String,
    @ColumnInfo(name = "preferred_theme")
    val preferredTheme: String,
    @ColumnInfo(name = "biometric_lock_enabled")
    val biometricLockEnabled: Boolean,
    @ColumnInfo(name = "overlay_enabled")
    val overlayEnabled: Boolean,
    @ColumnInfo(name = "quiet_hours_start")
    val quietHoursStart: String,
    @ColumnInfo(name = "quiet_hours_end")
    val quietHoursEnd: String,
)
