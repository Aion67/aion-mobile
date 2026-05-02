package com.example.aion.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aion.data.converters.AionConverters
import com.example.aion.data.dao.*
import com.example.aion.data.entities.*

@Database(
    entities = [
        TrackedAppEntity::class,
        AppSettingsEntity::class,
        UsageSessionEntity::class,
        NotificationEntity::class,
        UserProfileEntity::class,
        UserPreferenceEntity::class,
        UserPointsEntity::class,
        PointsTransactionEntity::class,
        ShopItemEntity::class,
        UserInventoryEntity::class,
        AchievementEntity::class,
        UserAchievementEntity::class,
        SquadEntity::class,
        SquadMemberEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(AionConverters::class)
abstract class AionDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
    abstract fun usageDao(): UsageDao
    abstract fun notificationDao(): NotificationDao
    abstract fun userDao(): UserDao
    abstract fun gamificationDao(): GamificationDao
    abstract fun shopDao(): ShopDao
    abstract fun socialDao(): SocialDao
}
