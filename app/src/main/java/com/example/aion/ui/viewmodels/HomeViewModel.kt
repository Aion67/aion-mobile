package com.example.aion.ui.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.TrackedAppEntity
import com.example.aion.data.repository.AppRepository
import com.example.aion.data.repository.UsageRepository
import com.example.aion.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class HomeUiState(
    val trackedApps: List<TrackedAppUsage> = emptyList(),
    val score: Float = 0f,
    val improvementPercentage: Float = 0f,
    val displayName: String = "User",
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
    private val usageRepository: UsageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val pm = context.packageManager

    val uiState: StateFlow<HomeUiState> = combine(
        appRepository.getAllTrackedApps(),
        userRepository.getUserProfile()
    ) { apps, profile ->
        Pair(apps, profile)
    }.flatMapLatest { (apps, profile) ->
        if (apps.isEmpty()) {
            flowOf(HomeUiState(displayName = profile?.displayName ?: "User"))
        } else {
            val todayStart = getTodayStartMs()
            val yesterdayStart = todayStart - 24 * 60 * 60 * 1000L
            
            val usageFlows = apps.map { app ->
                combine(
                    usageRepository.getTotalUsageForApp(app.packageName, todayStart),
                    usageRepository.getTotalUsageForAppInRange(app.packageName, yesterdayStart, todayStart),
                    appRepository.getSettingsForApp(app.packageName)
                ) { todayUsage, yesterdayUsage, settings ->
                    val icon = try {
                        pm.getApplicationIcon(app.packageName)
                    } catch (e: Exception) {
                        null
                    }
                    Triple(
                        TrackedAppUsage(app, icon, todayUsage ?: 0L, settings?.dailyLimitMs ?: 0L),
                        yesterdayUsage ?: 0L,
                        settings?.dailyLimitMs ?: 0L
                    )
                }
            }
            combine(usageFlows) { triples ->
                val usages = triples.map { it.first }
                val totalTodayUsage = usages.sumOf { it.usageMs }
                val totalTodayLimit = usages.sumOf { it.limitMs }
                val totalYesterdayUsage = triples.sumOf { it.second }
                
                // Default Score calculation: 100 - (usage/limit * 100), capped at 0-100
                // User requested default scores to be zero. 
                // Showing 0 if no total limit set OR no total usage recorded today.
                val score = if (totalTodayLimit > 0L && totalTodayUsage > 0L) {
                    val ratio = totalTodayUsage.toFloat() / totalTodayLimit
                    (100f * (1f - ratio)).coerceIn(0f, 100f)
                } else {
                    0f // Default to 0 as requested
                }

                // Improvement: (Yesterday - Today) / Yesterday
                val improvement = if (totalYesterdayUsage > 0L) {
                    (totalYesterdayUsage - totalTodayUsage).toFloat() / totalYesterdayUsage
                } else if (totalTodayUsage == 0L) {
                    0f
                } else {
                    -1f // Today is worse since we had 0 yesterday
                }

                HomeUiState(
                    trackedApps = usages.toList(),
                    score = score,
                    improvementPercentage = improvement,
                    displayName = profile?.displayName ?: "User"
                )
            }
        }
    }.stateIn(
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
