package com.example.aion.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aion.data.entities.NotificationEntity
import com.example.aion.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class NotificationDetailUiState(
    val notification: NotificationEntity? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class NotificationDetailViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val notificationId: Long = checkNotNull(savedStateHandle["id"])

    val uiState: StateFlow<NotificationDetailUiState> =
        notificationRepository.getNotificationById(notificationId)
            .map { notification ->
                NotificationDetailUiState(
                    notification = notification,
                    isLoading = false
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = NotificationDetailUiState()
            )
}
