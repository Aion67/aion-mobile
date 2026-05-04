package com.example.aion.ui.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.TrackedAppEntity
import com.example.aion.data.repository.AppRepository
import com.example.aion.data.repository.UsageRepository
import com.example.aion.data.repository.UserRepository
import com.example.aion.util.ScoreUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class HomeUiState(
    val trackedApps: List<TrackedAppUsage> = emptyList(),
    val score: Float = 0f,
    val improvementPercentage: Float = 0f,
    val weeklyImprovementPercentage: Float = 0f,
    val totalTimeSavedMs: Long = 0L,
    val rank: String = "Beginner",
    val displayName: String = "User",
    val isLoading: Boolean = false
)

data class TrackedAppUsage(
    val app: TrackedAppEntity,
    val icon: Drawable?,
    val usageMs: Long,
    val limitMs: Long,
    val score: Float = 0f
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
            val weekStart = todayStart - 7 * 24 * 60 * 60 * 1000L
            
            val usageFlows = apps.map { app ->
                combine(
                    usageRepository.getTotalUsageForApp(app.packageName, todayStart),
                    usageRepository.getTotalUsageForAppInRange(app.packageName, yesterdayStart, todayStart),
                    usageRepository.getTotalUsageForAppInRange(app.packageName, weekStart, todayStart),
                    appRepository.getSettingsForApp(app.packageName)
                ) { todayUsage, yesterdayUsage, weekUsage, settings ->
                    val icon = try {
                        pm.getApplicationIcon(app.packageName)
                    } catch (e: Exception) {
                        null
                    }
                    val today = todayUsage ?: 0L
                    val limit = settings?.dailyLimitMs ?: 0L
                    
                    val appScore = ScoreUtils.calculateScore(today, limit)

                    DataSnapshot(
                        usage = TrackedAppUsage(app, icon, today, limit, appScore),
                        yesterdayUsage = yesterdayUsage ?: 0L,
                        weekUsage = weekUsage ?: 0L
                    )
                }
            }
            combine(usageFlows) { snapshots ->
                val usages = snapshots.map { it.usage }
                val totalTodayUsage = usages.sumOf { it.usageMs }
                val totalTodayLimit = usages.sumOf { it.limitMs }
                val totalYesterdayUsage = snapshots.sumOf { it.yesterdayUsage }
                val totalWeekUsage = snapshots.sumOf { it.weekUsage }
                
                val score = ScoreUtils.calculateScore(totalTodayUsage, totalTodayLimit)
                val improvement = ScoreUtils.calculateImprovement(totalYesterdayUsage, totalTodayUsage)
                val weeklyImprovement = ScoreUtils.calculateImprovement(totalWeekUsage / 7, totalTodayUsage)

                val timeSaved = if (totalTodayLimit > totalTodayUsage) totalTodayLimit - totalTodayUsage else 0L

                val rank = when {
                    score >= 90 -> "Legend"
                    score >= 70 -> "Expert"
                    score >= 50 -> "Pro"
                    score >= 30 -> "Intermediate"
                    else -> "Beginner"
                }

                HomeUiState(
                    trackedApps = usages.toList(),
                    score = score,
                    improvementPercentage = improvement,
                    weeklyImprovementPercentage = weeklyImprovement,
                    totalTimeSavedMs = timeSaved,
                    rank = rank,
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

    private data class DataSnapshot(
        val usage: TrackedAppUsage,
        val yesterdayUsage: Long,
        val weekUsage: Long
    )
}
