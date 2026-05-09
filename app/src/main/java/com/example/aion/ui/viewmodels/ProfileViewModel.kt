package com.example.aion.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.UserProfileEntity
import com.example.aion.data.repository.AppRepository
import com.example.aion.data.repository.UsageRepository
import com.example.aion.data.repository.UserRepository
import com.example.aion.util.ScoringEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class ProfileUiState(
    val profile: UserProfileEntity = UserProfileEntity(username = "User", displayName = "New User"),
    val rank: String = "Beginner",
    val timeSavedMs: Long = 0L,
    val isLoading: Boolean = false
)

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appRepository: AppRepository,
    private val usageRepository: UsageRepository
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        userRepository.getUserProfile(),
        appRepository.getAllTrackedApps()
    ) { profile, apps ->
        profile to apps
    }.flatMapLatest { (profile, apps) ->
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        if (apps.isEmpty()) {
            flowOf(ProfileUiState(profile = profile ?: UserProfileEntity(username = "User", displayName = "User")))
        } else {
            val usageFlows = apps.map { app ->
                combine(
                    usageRepository.getTotalUsageForApp(app.packageName, todayStart),
                    appRepository.getSettingsForApp(app.packageName)
                ) { usage, settings ->
                    (usage ?: 0L) to (settings?.dailyLimitMs ?: 0L)
                }
            }

            combine(usageFlows) { results ->
                val totalTodayUsage = results.sumOf { it.first }
                val totalTodayLimit = results.sumOf { it.second }

                val score = ScoringEngine.calculateAppScore(totalTodayUsage, totalTodayLimit)
                val timeSaved = if (totalTodayLimit > totalTodayUsage) totalTodayLimit - totalTodayUsage else 0L

                val rank = when {
                    score >= 90 -> "Legend"
                    score >= 70 -> "Expert"
                    score >= 50 -> "Pro"
                    score >= 30 -> "Intermediate"
                    else -> "Beginner"
                }

                ProfileUiState(
                    profile = profile ?: UserProfileEntity(username = "User", displayName = "User"),
                    rank = rank,
                    timeSavedMs = timeSaved
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState(isLoading = true)
    )

    fun updateDisplayName(displayName: String) {
        viewModelScope.launch {
            val current = uiState.value.profile
            userRepository.saveUserProfile(current.copy(displayName = displayName))
        }
    }

    fun updateAvatar(uri: String) {
        viewModelScope.launch {
            val current = uiState.value.profile
            userRepository.saveUserProfile(current.copy(avatarUri = uri))
        }
    }
}
