package com.example.enaf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tracked_app",
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
        Index(value = ["user_id", "package_name"], unique = true),
    ]
)
data class TrackedAppEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "package_name")
    val packageName: String,
    @ColumnInfo(name = "alias")
    val alias: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "is_blocked")
    val isBlocked: Boolean,
    @ColumnInfo(name = "is_ignored")
    val isIgnored: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAtEpochMillis: Long,
)
