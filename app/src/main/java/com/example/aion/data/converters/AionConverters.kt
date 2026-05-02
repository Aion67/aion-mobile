package com.example.aion.data.converters

import androidx.room.TypeConverter
import com.example.aion.data.entities.NotificationType
import com.example.aion.data.entities.ShopItemType

class AionConverters {
    @TypeConverter
    fun fromNotificationType(value: NotificationType): String {
        return value.name
    }

    @TypeConverter
    fun toNotificationType(value: String): NotificationType {
        return NotificationType.valueOf(value)
    }

    @TypeConverter
    fun fromShopItemType(value: ShopItemType): String {
        return value.name
    }

    @TypeConverter
    fun toShopItemType(value: String): ShopItemType {
        return ShopItemType.valueOf(value)
    }
}
