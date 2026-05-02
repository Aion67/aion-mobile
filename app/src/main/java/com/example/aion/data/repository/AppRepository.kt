package com.example.aion.data.repository

import com.example.aion.data.dao.AppDao
import com.example.aion.data.entities.TrackedAppEntity
import com.example.aion.data.entities.AppSettingsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface AppRepository {
    fun getAllTrackedApps(): Flow<List<TrackedAppEntity>>
    suspend fun getTrackedApp(packageName: String): TrackedAppEntity?
    suspend fun addTrackedApp(app: TrackedAppEntity)
    fun getSettingsForApp(packageName: String): Flow<AppSettingsEntity?>
    suspend fun updateAppSettings(settings: AppSettingsEntity)
    suspend fun removeTrackedApp(packageName: String)
}

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val appDao: AppDao
) : AppRepository {
    override fun getAllTrackedApps() = appDao.getAllTrackedApps()

    override suspend fun getTrackedApp(packageName: String): TrackedAppEntity? {
        return appDao.getTrackedAppByPackageName(packageName)
    }
    
    override suspend fun addTrackedApp(app: TrackedAppEntity) {
        appDao.insertTrackedApp(app)
    }

    override fun getSettingsForApp(packageName: String) = appDao.getSettingsForApp(packageName)

    override suspend fun updateAppSettings(settings: AppSettingsEntity) {
        appDao.updateSettings(settings)
    }

    override suspend fun removeTrackedApp(packageName: String) {
        appDao.deleteTrackedApp(packageName)
    }
}
