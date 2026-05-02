package com.example.aion.ui.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.AppSettingsEntity
import com.example.aion.data.entities.TrackedAppEntity
import com.example.aion.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlanUiState(
    val trackedApps: List<TrackedAppWithSettings> = emptyList(),
    val isLoading: Boolean = false
)

data class TrackedAppWithSettings(
    val app: TrackedAppEntity,
    val settings: AppSettingsEntity,
    val icon: Drawable?
)

@HiltViewModel
class PlanViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appRepository: AppRepository
) : ViewModel() {

    private val pm = context.packageManager

    val uiState: StateFlow<PlanUiState> = appRepository.getAllTrackedApps()
        .flatMapLatest { apps ->
            if (apps.isEmpty()) {
                flowOf(PlanUiState())
            } else {
                val flows = apps.map { app ->
                    appRepository.getSettingsForApp(app.packageName).map { settings ->
                        val icon = try {
                            pm.getApplicationIcon(app.packageName)
                        } catch (e: Exception) {
                            null
                        }
                        TrackedAppWithSettings(app, settings ?: AppSettingsEntity(appPackageName = app.packageName), icon)
                    }
                }
                combine(flows) { list ->
                    PlanUiState(trackedApps = list.toList())
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlanUiState(isLoading = true)
        )

    fun updateSettings(settings: AppSettingsEntity) {
        viewModelScope.launch {
            appRepository.updateAppSettings(settings)
        }
    }

    fun removeApp(packageName: String) {
        viewModelScope.launch {
            appRepository.removeTrackedApp(packageName)
        }
    }
}
