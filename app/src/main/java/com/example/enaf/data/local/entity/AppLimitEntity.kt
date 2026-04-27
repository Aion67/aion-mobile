package com.example.enaf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "app_limit",
    foreignKeys = [
        ForeignKey(
            entity = TrackedAppEntity::class,
            parentColumns = ["id"],
            childColumns = ["tracked_app_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(value = ["tracked_app_id"])]
)
data class AppLimitEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "tracked_app_id")
    val trackedAppId: String,
    @ColumnInfo(name = "daily_limit_minutes")
    val dailyLimitMinutes: Int,
    @ColumnInfo(name = "warning_threshold_minutes")
    val warningThresholdMinutes: Int,
    @ColumnInfo(name = "reset_policy")
    val resetPolicy: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
    @ColumnInfo(name = "updated_at")
    val updatedAtEpochMillis: Long,
)
