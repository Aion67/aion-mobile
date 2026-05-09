package com.example.aion.ui.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.TrackedAppEntity
import com.example.aion.data.repository.AppRepository
import com.example.aion.data.repository.UsageRepository
import com.example.aion.data.repository.UserRepository
import com.example.aion.util.ScoringEngine
import com.example.aion.util.TimeUtils
import com.example.aion.ui.components.StreakDay
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val trackedApps: List<TrackedAppUsage> = emptyList(),
    val score: Float = 0f,
    val improvementPercentage: Float = 0f,
    val weeklyImprovementPercentage: Float = 0f,
    val totalTimeSavedMs: Long = 0L,
    val rank: String = "Beginner",
    val displayName: String = "User",
    val streakDays: List<StreakDay> = emptyList(),
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
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
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
            val todayStart = TimeUtils.getTodayStartMs()
            val yesterdayStart = todayStart - 24 * 60 * 60 * 1000L
            val weekStart = todayStart - 7 * 24 * 60 * 60 * 1000L
            
            // For global streak, we need usage for the last 7 days for ALL apps.
            val historyFlow = usageRepository.getAllSessions()
            
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
                    
                    val appScore = ScoringEngine.calculateAppScore(today, limit)

                    DataSnapshot(
                        usage = TrackedAppUsage(app, icon, today, limit, appScore),
                        yesterdayUsage = yesterdayUsage ?: 0L,
                        weekUsage = weekUsage ?: 0L
                    )
                }
            }
            val snapshotsFlow: Flow<List<DataSnapshot>> = combine(usageFlows) { it.toList() }
            combine(snapshotsFlow, historyFlow) { snapshots, history ->
                val usages = snapshots.map { it.usage }
                val totalTodayUsage = usages.sumOf { it.usageMs }
                val totalTodayLimit = usages.sumOf { it.limitMs }
                val totalYesterdayUsage = snapshots.sumOf { it.yesterdayUsage }
                val totalWeekUsage = snapshots.sumOf { it.weekUsage }
                
                val score = ScoringEngine.calculateAppScore(totalTodayUsage, totalTodayLimit)
                val improvement = ScoringEngine.calculateImprovement(totalYesterdayUsage, totalTodayUsage)
                val weeklyImprovement = ScoringEngine.calculateImprovement(totalWeekUsage / 7, totalTodayUsage)

                val timeSaved = if (totalTodayLimit > totalTodayUsage) totalTodayLimit - totalTodayUsage else 0L

                val rank = when {
                    score >= 90 -> "Legend"
                    score >= 70 -> "Expert"
                    score >= 50 -> "Pro"
                    score >= 30 -> "Intermediate"
                    else -> "Beginner"
                }

                val streakDays = calculateGlobalStreak(history, snapshots.associate { it.usage.app.packageName to it.usage.limitMs })

                HomeUiState(
                    trackedApps = usages.toList(),
                    score = score,
                    improvementPercentage = improvement,
                    weeklyImprovementPercentage = weeklyImprovement,
                    totalTimeSavedMs = timeSaved,
                    rank = rank,
                    displayName = profile?.displayName ?: "User",
                    streakDays = streakDays
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun removeApp(packageName: String) {
        viewModelScope.launch {
            appRepository.removeTrackedApp(packageName)
        }
    }

    private fun calculateGlobalStreak(
        history: List<com.example.aion.data.entities.UsageSessionEntity>,
        appLimits: Map<String, Long>
    ): List<StreakDay> {
        val days = mutableListOf<StreakDay>()
        val dayNameFormat = SimpleDateFormat("EEE", Locale.US)
        val dateNumberFormat = SimpleDateFormat("dd", Locale.US)
        
        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            
            val startOfDay = cal.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val endOfDay = startOfDay + 24 * 60 * 60 * 1000L
            
            val daySessions = history.filter { it.startTime >= startOfDay && it.startTime < endOfDay }
            
            // Check if any app exceeded its limit on this day
            var failed = false
            var hasAnyLimit = false
            for ((pkg, limit) in appLimits) {
                if (limit > 0) {
                    hasAnyLimit = true
                    val appUsage = daySessions.filter { it.appPackageName == pkg }.sumOf { it.totalDurationMs }
                    if (appUsage > limit) {
                        failed = true
                        break
                    }
                }
            }
            
            val isCompleted = hasAnyLimit && !failed
            days.add(
                StreakDay(
                    dayName = dayNameFormat.format(cal.time),
                    dateNumber = dateNumberFormat.format(cal.time),
                    isCompleted = isCompleted,
                    isToday = i == 0
                )
            )
        }
        return days
    }

    private data class DataSnapshot(
        val usage: TrackedAppUsage,
        val yesterdayUsage: Long,
        val weekUsage: Long
    )
}
