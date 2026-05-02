package com.example.aion.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.UserProfileEntity
import com.example.aion.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val profile: UserProfileEntity = UserProfileEntity(username = "New User"),
    val isLoading: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = userRepository.getUserProfile()
        .map { profile -> 
            ProfileUiState(profile = profile ?: UserProfileEntity(username = "User")) 
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState(isLoading = true)
        )

    fun updateUsername(username: String) {
        viewModelScope.launch {
            val current = uiState.value.profile
            userRepository.saveUserProfile(current.copy(username = username))
        }
    }

    fun updateAvatar(uri: String) {
        viewModelScope.launch {
            val current = uiState.value.profile
            userRepository.saveUserProfile(current.copy(avatarUri = uri))
        }
    }
}
