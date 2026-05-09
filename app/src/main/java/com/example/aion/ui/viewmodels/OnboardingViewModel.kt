package com.example.aion.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.UserPreferenceEntity
import com.example.aion.data.entities.UserProfileEntity
import com.example.aion.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val isCompleted: Boolean = false,
    val usagePermissionGranted: Boolean = false,
    val overlayPermissionGranted: Boolean = false,
    val displayName: String = "",
    val appMode: String = "offline"
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = combine(
        _uiState,
        userRepository.getPreference("onboarding_completed")
    ) { state, pref ->
        state.copy(isCompleted = pref?.value?.toBoolean() ?: false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OnboardingUiState())

    fun updatePermissionStatus(usage: Boolean, overlay: Boolean) {
        _uiState.update { it.copy(usagePermissionGranted = usage, overlayPermissionGranted = overlay) }
    }

    fun updateDisplayName(name: String) {
        _uiState.update { it.copy(displayName = name) }
    }

    fun updateAppMode(mode: String) {
        _uiState.update { it.copy(appMode = mode) }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            userRepository.savePreference(UserPreferenceEntity("app_mode", _uiState.value.appMode))
            userRepository.savePreference(UserPreferenceEntity("onboarding_completed", "true"))
            if (_uiState.value.displayName.isNotBlank()) {
                userRepository.saveUserProfile(
                    UserProfileEntity(
                        username = "user_${System.currentTimeMillis()}",
                        displayName = _uiState.value.displayName
                    )
                )
            }
        }
    }
}
