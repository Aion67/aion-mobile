package com.example.enaf.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.local.entity.SettingsEntity
import com.example.enaf.data.repository.LocalRepository
import com.example.enaf.ui.components.toUiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null
    private var settingsId: String? = null

    init {
        observeActiveSession()
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.RegretSimulationChanged -> {
                _uiState.value = _uiState.value.copy(regretSimulationEnabled = event.enabled)
                persistSettings()
            }
            is SettingsUiEvent.OpportunityLeakChanged -> {
                _uiState.value = _uiState.value.copy(opportunityLeakEnabled = event.enabled)
                persistSettings()
            }
            is SettingsUiEvent.SmartAlertsChanged -> {
                _uiState.value = _uiState.value.copy(smartAlertsEnabled = event.enabled)
                persistSettings()
            }
            is SettingsUiEvent.PulseNotificationsChanged -> {
                _uiState.value = _uiState.value.copy(pulseNotificationsEnabled = event.enabled)
                persistSettings()
            }
            is SettingsUiEvent.QuietDurationChanged -> {
                _uiState.value = _uiState.value.copy(quietModeDurationHours = event.hours)
                persistSettings()
            }
            is SettingsUiEvent.ThemeSelected -> {
                _uiState.value = _uiState.value.copy(selectedTheme = event.mode)
                persistSettings()
            }
            SettingsUiEvent.SignOutClicked -> signOut()
            SettingsUiEvent.Refresh -> refresh()
        }
    }

    private fun observeActiveSession() {
        viewModelScope.launch {
            localRepository.observeActiveSession().collect { session ->
                val userId = session?.userId
                if (userId == null) {
                    _uiState.value = settingsPreviewState().copy(
                        profile = SettingsProfileUiModel(
                            displayName = "Guest",
                            email = "guest@local",
                            tierLabel = "RECRUIT",
                        ),
                        isLoading = false,
                    )
                    return@collect
                }

                currentUserId = userId
                loadSettingsState(userId)
            }
        }
    }

    private fun refresh() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            loadSettingsState(userId)
        }
    }

    private suspend fun loadSettingsState(userId: String) {
        try {
            val settings = localRepository.getSettings(userId)
            settingsId = settings?.id ?: "settings-$userId"

            _uiState.value = SettingsUiState(
                profile = SettingsProfileUiModel(
                    displayName = "Warrior ${userId.takeLast(4)}",
                    email = "$userId@enaf.local",
                    tierLabel = inferTier(userId),
                ),
                regretSimulationEnabled = settings?.overlayEnabled ?: true,
                opportunityLeakEnabled = settings?.biometricLockEnabled ?: false,
                smartAlertsEnabled = settings?.allowNotifications ?: true,
                pulseNotificationsEnabled = settings?.allowDisplayOverOtherApps ?: true,
                quietModeDurationHours = parseDurationHours(settings?.quietHoursStart, settings?.quietHoursEnd),
                selectedTheme = parseThemeMode(settings?.themeMode),
                isLoading = false,
                error = null,
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = e.toUiError(),
            )
        }
    }

    private fun persistSettings() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            val state = _uiState.value
            val durationHours = state.quietModeDurationHours.toInt().coerceIn(0, 8)
            val entity = SettingsEntity(
                id = settingsId ?: "settings-$userId",
                userId = userId,
                allowNotifications = state.smartAlertsEnabled,
                allowDisplayOverOtherApps = state.pulseNotificationsEnabled,
                themeMode = when (state.selectedTheme) {
                    ThemeModeOption.LIGHT -> "light"
                    ThemeModeOption.DARK -> "dark"
                    ThemeModeOption.CELESTIAL -> "celestial"
                },
                preferredTheme = when (state.selectedTheme) {
                    ThemeModeOption.LIGHT -> "light"
                    ThemeModeOption.DARK -> "dark"
                    ThemeModeOption.CELESTIAL -> "celestial"
                },
                biometricLockEnabled = state.opportunityLeakEnabled,
                overlayEnabled = state.regretSimulationEnabled,
                quietHoursStart = "22:00",
                quietHoursEnd = String.format("%02d:00", (22 + durationHours) % 24),
            )
            localRepository.upsertSettings(entity)
            settingsId = entity.id
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            localRepository.clearSession()
        }
    }

    private fun inferTier(userId: String): String {
        return if (userId.contains("guest", ignoreCase = true)) "RECRUIT" else "VETERAN"
    }

    private fun parseThemeMode(value: String?): ThemeModeOption {
        return when (value?.lowercase()) {
            "light" -> ThemeModeOption.LIGHT
            "celestial" -> ThemeModeOption.CELESTIAL
            else -> ThemeModeOption.DARK
        }
    }

    private fun parseDurationHours(start: String?, end: String?): Float {
        val startHour = start?.substringBefore(':')?.toIntOrNull() ?: 22
        val endHour = end?.substringBefore(':')?.toIntOrNull() ?: 0
        val duration = if (endHour >= startHour) endHour - startHour else (24 - startHour) + endHour
        return duration.toFloat().coerceIn(0f, 8f)
    }
}
