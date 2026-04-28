package com.example.enaf.ui.components

/**
 * Sealed class for handling different error types across the app.
 * Allows for granular error handling and user-facing categorization.
 */
sealed class UiError {
    data class NetworkError(val message: String = "Network connection failed") : UiError()
    data class DatabaseError(val message: String = "Database operation failed") : UiError()
    data class ValidationError(val message: String) : UiError()
    data class AuthenticationError(val message: String = "Authentication failed") : UiError()
    data class PermissionError(val message: String = "Permission denied") : UiError()
    data class NotFoundError(val message: String = "Resource not found") : UiError()
    data class TimeoutError(val message: String = "Operation timed out") : UiError()
    data class UnknownError(val message: String = "An unexpected error occurred") : UiError()

    val userMessage: String
        get() = when (this) {
            is NetworkError -> message
            is DatabaseError -> "Failed to load data. Please try again."
            is ValidationError -> message
            is AuthenticationError -> message
            is PermissionError -> message
            is NotFoundError -> message
            is TimeoutError -> "Request took too long. Please try again."
            is UnknownError -> message
        }
}

// Helper function to convert exceptions to UiError
fun Throwable.toUiError(): UiError = when (this) {
    // Future: Add specific exception mapping here
    else -> UiError.UnknownError(this.message ?: "An unknown error occurred")
}
