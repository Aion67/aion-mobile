package com.example.enaf.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.enaf.data.local.entity.AchievementCacheEntity
import com.example.enaf.data.local.entity.AppLimitEntity
import com.example.enaf.data.local.entity.DailySummaryEntity
import com.example.enaf.data.local.entity.SettingsEntity
import com.example.enaf.data.local.entity.TrackedAppEntity
import com.example.enaf.data.local.entity.UsageSessionEntity
import com.example.enaf.data.local.entity.UserSessionEntity
import com.example.enaf.data.local.entity.WalletLedgerEntity

@Database(
    entities = [
        UserSessionEntity::class,
        TrackedAppEntity::class,
        AppLimitEntity::class,
        UsageSessionEntity::class,
        DailySummaryEntity::class,
        SettingsEntity::class,
        AchievementCacheEntity::class,
        WalletLedgerEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class EnafDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME: String = "enaf.db"
    }
}
