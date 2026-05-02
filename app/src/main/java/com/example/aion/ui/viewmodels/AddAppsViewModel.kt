package com.example.aion.ui.viewmodels

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.AppSettingsEntity
import com.example.aion.data.entities.TrackedAppEntity
import com.example.aion.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class AddAppsUiState(
    val apps: List<InstallableApp> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)

data class InstallableApp(
    val packageName: String,
    val name: String,
    val icon: Drawable?,
    val isTracked: Boolean = false
)

@HiltViewModel
class AddAppsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appRepository: AppRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<AddAppsUiState> = combine(
        _searchQuery,
        appRepository.getAllTrackedApps(),
        _isLoading
    ) { query, trackedApps, loading ->
        if (loading) return@combine AddAppsUiState(isLoading = true)
        
        val installedApps = getInstalledApps()
        val trackedPackageNames = trackedApps.map { it.packageName }.toSet()
        
        val filtered = installedApps.filter { 
            it.name.contains(query, ignoreCase = true) || it.packageName.contains(query, ignoreCase = true)
        }.map { app ->
            app.copy(isTracked = trackedPackageNames.contains(app.packageName))
        }
        
        AddAppsUiState(apps = filtered, searchQuery = query, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AddAppsUiState(isLoading = true)
    )

    private suspend fun getInstalledApps(): List<InstallableApp> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        apps.filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
            .map { appInfo ->
                InstallableApp(
                    packageName = appInfo.packageName,
                    name = pm.getApplicationLabel(appInfo).toString(),
                    icon = pm.getApplicationIcon(appInfo)
                )
            }.sortedBy { it.name }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleTracking(app: InstallableApp) {
        viewModelScope.launch {
            if (app.isTracked) {
                appRepository.removeTrackedApp(app.packageName)
            } else {
                appRepository.addTrackedApp(
                    TrackedAppEntity(
                        packageName = app.packageName,
                        appName = app.name
                        // iconUri can be handled later
                    )
                )
                appRepository.updateAppSettings(
                    AppSettingsEntity(
                        appPackageName = app.packageName,
                        dailyLimitMs = 3600000 // Default 1 hour
                    )
                )
            }
        }
    }
}
