package com.example.aion.ui.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.AppSettingsEntity
import com.example.aion.data.entities.TrackedAppEntity
import com.example.aion.data.repository.AppRepository
import com.example.aion.data.repository.UsageRepository
import com.example.aion.util.ScoreUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class PlanSort {
    NAME,
    LIMIT,
    DATE_ADDED
}

data class PlanUiState(
    val trackedApps: List<TrackedAppWithSettings> = emptyList(),
    val currentSort: PlanSort = PlanSort.NAME,
    val isLoading: Boolean = false
)

data class TrackedAppWithSettings(
    val app: TrackedAppEntity,
    val settings: AppSettingsEntity,
    val icon: Drawable?,
    val usageMs: Long = 0L,
    val score: Float = 0f
)

@HiltViewModel
class PlanViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appRepository: AppRepository,
    private val usageRepository: UsageRepository
) : ViewModel() {

    private val pm = context.packageManager

    private val _currentSort = MutableStateFlow(PlanSort.NAME)

    val uiState: StateFlow<PlanUiState> = combine(
        appRepository.getAllTrackedApps(),
        _currentSort
    ) { apps, sort ->
        apps to sort
    }.flatMapLatest { (apps, sort) ->
            if (apps.isEmpty()) {
                flowOf(PlanUiState(currentSort = sort))
            } else {
                val flows = apps.map { app ->
                    combine(
                        appRepository.getSettingsForApp(app.packageName),
                        usageRepository.getTotalUsageForApp(app.packageName, getTodayStartMs())
                    ) { settings, usage ->
                        val icon = try {
                            pm.getApplicationIcon(app.packageName)
                        } catch (e: Exception) {
                            null
                        }
                        val todayUsage = usage ?: 0L
                        val limit = settings?.dailyLimitMs ?: 0L
                        val score = ScoreUtils.calculateScore(todayUsage, limit)

                        TrackedAppWithSettings(
                            app, 
                            settings ?: AppSettingsEntity(appPackageName = app.packageName), 
                            icon,
                            todayUsage,
                            score
                        )
                    }
                }
                combine(flows) { list ->
                    val sortedList = when (sort) {
                        PlanSort.NAME -> list.sortedBy { it.app.appName }
                        PlanSort.LIMIT -> list.sortedByDescending { it.settings.dailyLimitMs }
                        PlanSort.DATE_ADDED -> list.sortedByDescending { it.app.addedDate }
                    }
                    PlanUiState(trackedApps = sortedList, currentSort = sort)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlanUiState(isLoading = true)
        )

    fun setSort(sort: PlanSort) {
        _currentSort.value = sort
    }

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

    private fun getTodayStartMs(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
