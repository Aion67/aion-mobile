package com.example.enaf.ui.screens.settings

import kotlin.collections.listOf

enum class ThemeModeOption {
    LIGHT,
    DARK,
    CELESTIAL,
}

data class SettingsProfileUiModel(
    val displayName: String,
    val email: String,
    val tierLabel: String,
)

data class SettingsUiState(
    val profile: SettingsProfileUiModel = SettingsProfileUiModel("", "", ""),
    val regretSimulationEnabled: Boolean = true,
    val opportunityLeakEnabled: Boolean = false,
    val smartAlertsEnabled: Boolean = true,
    val pulseNotificationsEnabled: Boolean = true,
    val quietModeDurationHours: Float = 2f,
    val selectedTheme: ThemeModeOption = ThemeModeOption.DARK,
    val isLoading: Boolean = true,
)

sealed interface SettingsUiEvent {
    data class RegretSimulationChanged(val enabled: Boolean) : SettingsUiEvent
    data class OpportunityLeakChanged(val enabled: Boolean) : SettingsUiEvent
    data class SmartAlertsChanged(val enabled: Boolean) : SettingsUiEvent
    data class PulseNotificationsChanged(val enabled: Boolean) : SettingsUiEvent
    data class QuietDurationChanged(val hours: Float) : SettingsUiEvent
    data class ThemeSelected(val mode: ThemeModeOption) : SettingsUiEvent
    data object SignOutClicked : SettingsUiEvent
    data object Refresh : SettingsUiEvent
}

fun settingsPreviewState(): SettingsUiState {
    return SettingsUiState(
        profile = SettingsProfileUiModel(
            displayName = "Winzer Prince",
            email = "aita.josh@example.com",
            tierLabel = "PRO MEMBER",
        ),
        regretSimulationEnabled = true,
        opportunityLeakEnabled = false,
        smartAlertsEnabled = true,
        pulseNotificationsEnabled = true,
        quietModeDurationHours = 2f,
        selectedTheme = ThemeModeOption.DARK,
        isLoading = false,
    )
}
