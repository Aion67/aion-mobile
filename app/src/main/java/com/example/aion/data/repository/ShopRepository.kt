package com.example.aion.data.repository

import com.example.aion.data.dao.ShopDao
import com.example.aion.data.entities.ShopItemEntity
import com.example.aion.data.entities.UserInventoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface ShopRepository {
    fun getAllShopItems(): Flow<List<ShopItemEntity>>
    fun getShopItemsByType(type: String): Flow<List<ShopItemEntity>>
    suspend fun addShopItems(items: List<ShopItemEntity>)
    fun getUserInventory(): Flow<List<UserInventoryEntity>>
    suspend fun purchaseItem(inventoryItem: UserInventoryEntity)
}

@Singleton
class ShopRepositoryImpl @Inject constructor(
    private val shopDao: ShopDao
) : ShopRepository {
    override fun getAllShopItems() = shopDao.getAllShopItems()

    override fun getShopItemsByType(type: String) = shopDao.getShopItemsByType(type)

    override suspend fun addShopItems(items: List<ShopItemEntity>) {
        shopDao.insertShopItems(items)
    }

    override fun getUserInventory() = shopDao.getUserInventory()

    override suspend fun purchaseItem(inventoryItem: UserInventoryEntity) {
        shopDao.purchaseItem(inventoryItem)
    }
}
