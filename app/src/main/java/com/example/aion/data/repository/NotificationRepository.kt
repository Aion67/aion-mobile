package com.example.aion.data.repository

import com.example.aion.data.dao.NotificationDao
import com.example.aion.data.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface NotificationRepository {
    fun getAllNotifications(): Flow<List<NotificationEntity>>
    suspend fun addNotification(notification: NotificationEntity)
    suspend fun markAllAsRead()
}

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {
    override fun getAllNotifications() = notificationDao.getAllNotifications()

    override suspend fun addNotification(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }

    override suspend fun markAllAsRead() {
        notificationDao.markAllAsRead()
    }
}
