package com.example.enaf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usage_session",
    foreignKeys = [
        ForeignKey(
            entity = TrackedAppEntity::class,
            parentColumns = ["id"],
            childColumns = ["tracked_app_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(value = ["tracked_app_id"]), Index(value = ["started_at"])]
)
data class UsageSessionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "tracked_app_id")
    val trackedAppId: String,
    @ColumnInfo(name = "started_at")
    val startedAtEpochMillis: Long,
    @ColumnInfo(name = "ended_at")
    val endedAtEpochMillis: Long,
    @ColumnInfo(name = "duration_minutes")
    val durationMinutes: Int,
    @ColumnInfo(name = "source")
    val source: String,
    @ColumnInfo(name = "was_interrupted")
    val wasInterrupted: Boolean,
)
