package com.example.enaf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_summary",
    foreignKeys = [
        ForeignKey(
            entity = UserSessionEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["user_id"]),
        Index(value = ["user_id", "summary_date"], unique = true),
    ]
)
data class DailySummaryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "summary_date")
    val summaryDate: String,
    @ColumnInfo(name = "focus_minutes")
    val focusMinutes: Int,
    @ColumnInfo(name = "scroll_minutes")
    val scrollMinutes: Int,
    @ColumnInfo(name = "time_saved_minutes")
    val timeSavedMinutes: Int,
    @ColumnInfo(name = "completion_rate")
    val completionRate: Float,
    @ColumnInfo(name = "credit_score")
    val creditScore: Int,
    @ColumnInfo(name = "streak_day")
    val streakDay: Int,
)
