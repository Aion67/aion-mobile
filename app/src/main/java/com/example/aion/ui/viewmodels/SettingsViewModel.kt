package com.example.aion.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.UserPreferenceEntity
import com.example.aion.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val theme: String = "System",
    val accentColor: String = "Purple",
    val notificationsEnabled: Boolean = true,
    val appMode: String = "offline",
    val hasUsedOnlineMode: Boolean = false,
    val hasUsageAccess: Boolean = false,
    val hasOverlayPermission: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _permissionState = MutableStateFlow(Pair(false, false))

    val uiState: StateFlow<SettingsUiState> = combine(
        userRepository.getAllPreferences(),
        _permissionState
    ) { prefs, permissions ->
        val prefMap = prefs.associate { it.key to it.value }
        SettingsUiState(
            theme = prefMap["theme_mode"] ?: "System",
            accentColor = prefMap["accent_color"] ?: "Purple",
            notificationsEnabled = prefMap["notifications_enabled"]?.toBoolean() ?: true,
            appMode = prefMap["app_mode"] ?: "offline",
            hasUsedOnlineMode = prefMap["has_used_online_mode"]?.toBoolean() ?: false,
            hasUsageAccess = permissions.first,
            hasOverlayPermission = permissions.second
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState(isLoading = true)
    )

    fun updatePermissionStatus(usage: Boolean, overlay: Boolean) {
        _permissionState.value = Pair(usage, overlay)
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            userRepository.savePreference(UserPreferenceEntity("theme_mode", theme))
        }
    }

    fun updateAccentColor(color: String) {
        viewModelScope.launch {
            userRepository.savePreference(UserPreferenceEntity("accent_color", color))
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            userRepository.savePreference(UserPreferenceEntity("notifications_enabled", enabled.toString()))
        }
    }

    fun updateAppMode(mode: String) {
        viewModelScope.launch {
            userRepository.savePreference(UserPreferenceEntity("app_mode", mode))
            if (mode == "online") {
                userRepository.savePreference(UserPreferenceEntity("has_used_online_mode", "true"))
            }
        }
    }
}
