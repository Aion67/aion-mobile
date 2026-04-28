package com.example.enaf.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.enaf.data.local.dao.AchievementCacheDao
import com.example.enaf.data.local.dao.AppLimitDao
import com.example.enaf.data.local.dao.DailySummaryDao
import com.example.enaf.data.local.dao.SettingsDao
import com.example.enaf.data.local.dao.TrackedAppDao
import com.example.enaf.data.local.dao.UsageSessionDao
import com.example.enaf.data.local.dao.UserSessionDao
import com.example.enaf.data.local.dao.WalletLedgerDao
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
    version = 2,
    exportSchema = true,
)
abstract class EnafDatabase : RoomDatabase() {
    abstract fun userSessionDao(): UserSessionDao
    abstract fun trackedAppDao(): TrackedAppDao
    abstract fun appLimitDao(): AppLimitDao
    abstract fun usageSessionDao(): UsageSessionDao
    abstract fun dailySummaryDao(): DailySummaryDao
    abstract fun settingsDao(): SettingsDao
    abstract fun achievementCacheDao(): AchievementCacheDao
    abstract fun walletLedgerDao(): WalletLedgerDao

    companion object {
        const val DB_NAME: String = "enaf.db"
    }
}
