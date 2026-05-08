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
import com.example.aion.utils.PermissionUtils
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
    val searchQuery: String = "",
    val hasUsageAccess: Boolean = true
)

data class InstallableApp(
    val packageName: String,
    val name: String,
    val icon: Drawable?,
    val isTracked: Boolean = false
)

@HiltViewModel
class AddAppsViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val appRepository: AppRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    
    private val _isLoading = MutableStateFlow(false)
    
    private val _hasUsageAccess = MutableStateFlow(true)

    val uiState: StateFlow<AddAppsUiState> = combine(
        _searchQuery,
        appRepository.getAllTrackedApps(),
        _isLoading,
        _hasUsageAccess
    ) { query, trackedApps, loading, hasAccess ->
        if (loading) return@combine AddAppsUiState(isLoading = true, searchQuery = query, hasUsageAccess = hasAccess)
        
        val installedApps = getInstalledApps()
        val trackedPackageNames = trackedApps.map { it.packageName }.toSet()
        
        val filtered = installedApps.filter { 
            it.name.contains(query, ignoreCase = true) || it.packageName.contains(query, ignoreCase = true)
        }.map { app ->
            app.copy(isTracked = trackedPackageNames.contains(app.packageName))
        }
        
        AddAppsUiState(
            apps = filtered, 
            searchQuery = query, 
            isLoading = false,
            hasUsageAccess = hasAccess
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AddAppsUiState(isLoading = true)
    )

    init {
        checkPermissions()
    }

    fun checkPermissions() {
        _hasUsageAccess.value = PermissionUtils.hasUsageStatsPermission(context)
    }

    private suspend fun getInstalledApps(): List<InstallableApp> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        // Using getInstalledPackages(0) to get all packages. 
        // QUERY_ALL_PACKAGES is now in manifest to see non-system apps on API 30+
        val packages = pm.getInstalledPackages(0)
        
        packages.filter { pkgInfo ->
            val appInfo = pkgInfo.applicationInfo ?: return@filter false
            val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            val isUpdatedSystemApp = (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
            
            // Criteria: 
            // 1. Not a system app
            // 2. OR is an updated system app (like Chrome, YouTube)
            // 3. OR has a launch intent (user can open it)
            // 4. AND it's not this app itself
            val isThisApp = pkgInfo.packageName == context.packageName
            
            (!isSystemApp || isUpdatedSystemApp || pm.getLaunchIntentForPackage(pkgInfo.packageName) != null) && !isThisApp
        }.map { pkgInfo ->
            InstallableApp(
                packageName = pkgInfo.packageName,
                name = pm.getApplicationLabel(pkgInfo.applicationInfo!!).toString(),
                icon = pm.getApplicationIcon(pkgInfo.applicationInfo!!)
            )
        }.distinctBy { it.packageName }
         .sortedBy { it.name }
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
