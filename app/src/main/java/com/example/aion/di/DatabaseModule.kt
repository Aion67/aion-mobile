package com.example.aion.di

import android.content.Context
import androidx.room.Room
import com.example.aion.data.AionDatabase
import com.example.aion.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AionDatabase {
        return Room.databaseBuilder(
            context,
            AionDatabase::class.java,
            "aion_database"
        ).fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun provideAppDao(database: AionDatabase): AppDao = database.appDao()

    @Provides
    fun provideUsageDao(database: AionDatabase): UsageDao = database.usageDao()

    @Provides
    fun provideNotificationDao(database: AionDatabase): NotificationDao = database.notificationDao()

    @Provides
    fun provideUserDao(database: AionDatabase): UserDao = database.userDao()

    @Provides
    fun provideGamificationDao(database: AionDatabase): GamificationDao = database.gamificationDao()

    @Provides
    fun provideShopDao(database: AionDatabase): ShopDao = database.shopDao()

    @Provides
    fun provideSocialDao(database: AionDatabase): SocialDao = database.socialDao()
}
