package com.example.aion.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Stores basic information about a tracked application.
 */
@Entity(
    tableName = "tracked_apps",
    indices = [Index(value = ["packageName"], unique = true)]
)
data class TrackedAppEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageName: String,
    val appName: String,
    val iconUri: String? = null, // URI or resource name
    val addedDate: Long = System.currentTimeMillis()
)

/**
 * Stores user-defined settings for a specific tracked app.
 */
@Entity(
    tableName = "app_settings",
    foreignKeys = [
        ForeignKey(
            entity = TrackedAppEntity::class,
            parentColumns = ["packageName"],
            childColumns = ["appPackageName"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["appPackageName"], unique = true)]
)
data class AppSettingsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val appPackageName: String,
    val dailyLimitMs: Long = 0,
    val isTracked: Boolean = true,
    val notifyOnLimit: Boolean = true,
    val notifyOnHalfLimit: Boolean = false
)

/**
 * Represents a single usage session of an app.
 */
@Entity(
    tableName = "usage_sessions",
    foreignKeys = [
        ForeignKey(
            entity = TrackedAppEntity::class,
            parentColumns = ["packageName"],
            childColumns = ["appPackageName"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["appPackageName"])]
)
data class UsageSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val appPackageName: String,
    val startTime: Long,
    val endTime: Long,
    val totalDurationMs: Long
)

enum class NotificationType {
    LIMIT_REACHED,
    HALF_LIMIT_REACHED,
    INFO,
    WARNING,
    SUCCESS
}

/**
 * Stores notifications triggered by the app.
 */
@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val appPackageName: String? = null,
    val type: NotificationType = NotificationType.LIMIT_REACHED,
    val isRead: Boolean = false
)
