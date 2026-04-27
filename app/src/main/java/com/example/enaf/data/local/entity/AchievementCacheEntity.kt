package com.example.enaf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "achievement_cache",
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
        Index(value = ["user_id", "achievement_id"], unique = true),
    ]
)
data class AchievementCacheEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "achievement_id")
    val achievementId: String,
    @ColumnInfo(name = "unlocked_at")
    val unlockedAtEpochMillis: Long,
)
