package com.example.aion.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aion.data.entities.ShopItemEntity
import com.example.aion.data.entities.UserInventoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Query("SELECT * FROM shop_items")
    fun getAllShopItems(): Flow<List<ShopItemEntity>>

    @Query("SELECT * FROM shop_items WHERE type = :type")
    fun getShopItemsByType(type: String): Flow<List<ShopItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopItems(items: List<ShopItemEntity>)

    @Query("SELECT * FROM user_inventory")
    fun getUserInventory(): Flow<List<UserInventoryEntity>>

    @Insert
    suspend fun purchaseItem(inventoryItem: UserInventoryEntity)
}
