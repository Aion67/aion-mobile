package com.example.enaf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enaf.data.local.entity.WalletLedgerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletLedgerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: WalletLedgerEntity)

    @Query("SELECT * FROM wallet_ledger WHERE user_id = :userId LIMIT 1")
    fun observeByUser(userId: String): Flow<WalletLedgerEntity?>

    @Query("SELECT * FROM wallet_ledger WHERE user_id = :userId LIMIT 1")
    suspend fun getByUser(userId: String): WalletLedgerEntity?
}
