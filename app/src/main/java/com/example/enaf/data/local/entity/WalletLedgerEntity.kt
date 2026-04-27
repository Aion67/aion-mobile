package com.example.enaf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "wallet_ledger",
    foreignKeys = [
        ForeignKey(
            entity = UserSessionEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(value = ["user_id"], unique = true)]
)
data class WalletLedgerEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "coins_balance_cache")
    val coinsBalanceCache: Int,
    @ColumnInfo(name = "diamonds_balance_cache")
    val diamondsBalanceCache: Int,
    @ColumnInfo(name = "updated_at")
    val updatedAtEpochMillis: Long,
)
