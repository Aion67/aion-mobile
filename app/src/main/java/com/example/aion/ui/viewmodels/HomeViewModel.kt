package com.example.aion.ui.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.TrackedAppEntity
import com.example.aion.data.repository.AppRepository
import com.example.aion.data.repository.UsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class HomeUiState(
    val trackedApps: List<TrackedAppUsage> = emptyList(),
    val isLoading: Boolean = false
)

data class TrackedAppUsage(
    val app: TrackedAppEntity,
    val icon: Drawable?,
    val usageMs: Long,
    val limitMs: Long
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appRepository: AppRepository,
    private val usageRepository: UsageRepository
) : ViewModel() {

    private val pm = context.packageManager

    val uiState: StateFlow<HomeUiState> = appRepository.getAllTrackedApps()
        .flatMapLatest { apps ->
            if (apps.isEmpty()) {
                flowOf(HomeUiState())
            } else {
                val usageFlows = apps.map { app ->
                    combine(
                        usageRepository.getTotalUsageForApp(app.packageName, getTodayStartMs()),
                        appRepository.getSettingsForApp(app.packageName)
                    ) { usage, settings ->
                        val icon = try {
                            pm.getApplicationIcon(app.packageName)
                        } catch (e: Exception) {
                            null
                        }
                        TrackedAppUsage(app, icon, usage ?: 0L, settings?.dailyLimitMs ?: 0L)
                    }
                }
                combine(usageFlows) { usages ->
                    HomeUiState(trackedApps = usages.toList())
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoading = true)
        )

    private fun getTodayStartMs(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
