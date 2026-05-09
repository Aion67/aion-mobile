package com.example.aion.ui.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.AppSettingsEntity
import com.example.aion.data.entities.UsageSessionEntity
import com.example.aion.data.repository.AppRepository
import com.example.aion.data.repository.UsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.aion.ui.components.StreakDay
import com.example.aion.util.TimeUtils
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat
import javax.inject.Inject

data class AppDetailsUiState(
    val appName: String = "",
    val packageName: String = "",
    val icon: Drawable? = null,
    val iconUri: String? = null,
    val currentLimitMs: Long = 0,
    val currentIsTracked: Boolean = true,
    val pendingLimitMs: Long = 0,
    val pendingIsTracked: Boolean = true,
    val usageTodayMs: Long = 0,
    val history: List<UsageSessionEntity> = emptyList(),
    val streakDays: List<StreakDay> = emptyList(),
    val lastOpenedMs: Long = 0,
    val notoriety: String = "LOW",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val isLoading: Boolean = true
) {
    val isDirty: Boolean get() = currentLimitMs != pendingLimitMs || currentIsTracked != pendingIsTracked
}

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val appRepository: AppRepository,
    private val usageRepository: UsageRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val packageName: String = checkNotNull(savedStateHandle["packageName"])
    private val pm = context.packageManager

    private val _pendingState = MutableStateFlow<Pair<Long?, Boolean?>>(Pair(null, null))
    private val _saveSuccess = MutableStateFlow(false)
    private val _isSaving = MutableStateFlow(false)

    @Suppress("UNCHECKED_CAST")
    val uiState: StateFlow<AppDetailsUiState> = combine(
        getAppInfoFlow(),
        appRepository.getSettingsForApp(packageName),
        getUsageTodayFlow(),
        usageRepository.getSessionsForApp(packageName),
        _pendingState,
        _saveSuccess,
        _isSaving
    ) { args ->
        val appInfo = args[0] as Triple<String, String?, Drawable?>
        val settings = args[1] as AppSettingsEntity?
        val usageToday = args[2] as Long?
        val history = args[3] as List<UsageSessionEntity>
        val pending = args[4] as Pair<Long?, Boolean?>
        val saveSuccess = args[5] as Boolean
        val isSaving = args[6] as Boolean

        val limit = settings?.dailyLimitMs ?: 0L
        val tracked = settings?.isTracked ?: true
        val usage = usageToday ?: 0L
        
        val streakDays = calculateStreakDays(history, limit)
        val lastOpened = history.maxOfOrNull { it.endTime } ?: 0L
        
        val notoriety = if (limit > 0L) {
            val ratio = usage.toFloat() / limit.toFloat()
            when {
                ratio > 0.8f -> "HARD"
                ratio >= 0.5f -> "MODERATE"
                else -> "LOW"
            }
        } else {
            "LOW"
        }
        
        AppDetailsUiState(
            appName = appInfo.first,
            packageName = packageName,
            iconUri = appInfo.second,
            icon = appInfo.third,
            currentLimitMs = limit,
            currentIsTracked = tracked,
            pendingLimitMs = pending.first ?: limit,
            pendingIsTracked = pending.second ?: tracked,
            usageTodayMs = usage,
            history = history.sortedByDescending { it.startTime },
            streakDays = streakDays,
            lastOpenedMs = lastOpened,
            notoriety = notoriety,
            saveSuccess = saveSuccess,
            isSaving = isSaving,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppDetailsUiState(packageName = packageName, isLoading = true)
    )

    private fun calculateStreakDays(history: List<UsageSessionEntity>, limitMs: Long): List<StreakDay> {
        val today = Calendar.getInstance()
        val days = mutableListOf<StreakDay>()
        
        val dayNameFormat = SimpleDateFormat("EEE", Locale.US)
        val dateNumberFormat = SimpleDateFormat("dd", Locale.US)

        // Generate last 7 days (including today)
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
            
            val dayUsage = history.filter { it.startTime >= startOfDay && it.startTime < endOfDay }
                .sumOf { it.totalDurationMs }
            
            // Passed if usage <= limit and limit > 0
            val isCompleted = limitMs > 0 && dayUsage <= limitMs
            
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

    private fun getAppInfoFlow() = flow {
        val app = appRepository.getTrackedApp(packageName)
        val appIcon = try { pm.getApplicationIcon(packageName) } catch (e: Exception) { null }
        if (app != null) {
            emit(Triple(app.appName, app.iconUri, appIcon))
        } else {
            try {
                val appInfo = pm.getApplicationInfo(packageName, 0)
                emit(Triple(pm.getApplicationLabel(appInfo).toString(), null, appIcon))
            } catch (e: Exception) {
                emit(Triple(packageName, null, null))
            }
        }
    }

    private fun getUsageTodayFlow(): Flow<Long?> = flow {
        val todayStart = TimeUtils.getTodayStartMs()
        emitAll(usageRepository.getTotalUsageForApp(packageName, todayStart))
    }

    fun updatePendingLimit(hours: Int, minutes: Int, seconds: Int) {
        val ms = (hours * 3600 + minutes * 60 + seconds) * 1000L
        _pendingState.update { it.copy(first = ms) }
        _saveSuccess.value = false
    }

    fun updatePendingTracking(enabled: Boolean) {
        _pendingState.update { it.copy(second = enabled) }
        _saveSuccess.value = false
    }

    fun saveSettings() {
        val currentState = uiState.value
        if (!currentState.isDirty) return

        viewModelScope.launch {
            _isSaving.value = true
            
            val settings = appRepository.getSettingsForApp(packageName).firstOrNull()
            val updatedSettings = if (settings != null) {
                settings.copy(
                    dailyLimitMs = currentState.pendingLimitMs,
                    isTracked = currentState.pendingIsTracked
                )
            } else {
                AppSettingsEntity(
                    appPackageName = packageName,
                    dailyLimitMs = currentState.pendingLimitMs,
                    isTracked = currentState.pendingIsTracked
                )
            }
            appRepository.updateAppSettings(updatedSettings)
            
            _pendingState.value = Pair(null, null) // Reset pending to match current
            _isSaving.value = false
            _saveSuccess.value = true
        }
    }
    
    fun dismissSaveSuccess() {
        _saveSuccess.value = false
    }

    fun resetProgress() {
        viewModelScope.launch {
            val todayStart = TimeUtils.getTodayStartMs()
            usageRepository.resetTodayUsage(packageName, todayStart)
        }
    }
}
