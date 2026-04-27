package com.example.enaf.data.local

import android.content.Context
import androidx.room.Room

object EnafDatabaseProvider {
    @Volatile
    private var instance: EnafDatabase? = null

    fun get(context: Context): EnafDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                EnafDatabase::class.java,
                EnafDatabase.DB_NAME,
            ).fallbackToDestructiveMigration(false).build().also { db ->
                instance = db
            }
        }
    }
}
