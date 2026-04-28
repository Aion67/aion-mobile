package com.example.enaf.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object EnafDatabaseProvider {
    @Volatile
    private var instance: EnafDatabase? = null

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE settings ADD COLUMN global_limit_minutes INTEGER NOT NULL DEFAULT 240")
        }
    }

    fun get(context: Context): EnafDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                EnafDatabase::class.java,
                EnafDatabase.DB_NAME,
            ).addMigrations(MIGRATION_1_2).fallbackToDestructiveMigration(false).build().also { db ->
                instance = db
            }
        }
    }
}
