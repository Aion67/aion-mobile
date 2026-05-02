package com.example.aion.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

// --- GAMIFICATION ---

@Entity(tableName = "user_points")
data class UserPointsEntity(
    @PrimaryKey val userId: String = "default_user",
    val balance: Int = 0
)

@Entity(tableName = "points_transactions")
data class PointsTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Int,
    val reason: String, // e.g., "DAILY_GOAL_MET", "ACHIEVEMENT_UNLOCKED"
    val timestamp: Long = System.currentTimeMillis()
)

enum class ShopItemType {
    QUOTE,
    WALLPAPER,
    SOUND,
    THEME,
    AVATAR
}

// --- ARSENAL / SHOP ---

@Entity(tableName = "shop_items")
data class ShopItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val type: ShopItemType,
    val cost: Int,
    val content: String // Text or resource URI
)

@Entity(tableName = "user_inventory")
data class UserInventoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: String,
    val purchaseDate: Long = System.currentTimeMillis()
)

// --- REWARDS ---

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val name: String,
    val condition: String,
    val pointsReward: Int
)

@Entity(tableName = "user_achievements")
data class UserAchievementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val achievementId: String,
    val earnedDate: Long = System.currentTimeMillis()
)

// --- SOCIAL ---

@Entity(tableName = "squads")
data class SquadEntity(
    @PrimaryKey val id: String,
    val name: String,
    val createdBy: String
)

@Entity(tableName = "squad_members")
data class SquadMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val squadId: String,
    val username: String,
    val joinedDate: Long = System.currentTimeMillis()
)
