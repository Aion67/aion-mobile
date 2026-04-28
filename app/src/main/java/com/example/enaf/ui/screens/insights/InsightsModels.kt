package com.example.enaf.ui.screens.insights

import com.example.enaf.ui.components.UiError
import kotlin.collections.emptyList
import kotlin.collections.listOf

enum class InsightsRange {
    WEEKLY,
    MONTHLY,
    YEARLY,
}

data class InsightsCategoryUiModel(
    val label: String,
    val percentageLabel: String,
    val accentColor: Long,
)

data class InsightsObservationUiModel(
    val title: String,
    val description: String,
    val accentColor: Long,
)

data class InsightsUiState(
    val selectedRange: InsightsRange = InsightsRange.MONTHLY,
    val streakDays: Int = 0,
    val totalDaysLabel: String = "0 Days Total",
    val categories: List<InsightsCategoryUiModel> = emptyList(),
    val observations: List<InsightsObservationUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: UiError? = null,
)

sealed interface InsightsUiEvent {
    data class RangeSelected(val range: InsightsRange) : InsightsUiEvent
    data object Refresh : InsightsUiEvent
}

fun insightsPreviewState(): InsightsUiState {
    return InsightsUiState(
        selectedRange = InsightsRange.MONTHLY,
        streakDays = 24,
        totalDaysLabel = "24 Days Total",
        categories = listOf(
            InsightsCategoryUiModel("Focus", "42%", 0xFF007BFF),
            InsightsCategoryUiModel("Wellness", "28%", 0xFFD946EF),
            InsightsCategoryUiModel("Productivity", "18%", 0xFF00F2EA),
            InsightsCategoryUiModel("Social", "12%", 0xFFF97316),
        ),
        observations = listOf(
            InsightsObservationUiModel(
                title = "Morning Peak",
                description = "Your productivity is 40% higher between 8 AM and 11 AM. Schedule deep work then.",
                accentColor = 0xFF007BFF,
            ),
            InsightsObservationUiModel(
                title = "Weekend Drift",
                description = "Social media usage spikes on Saturdays. Consider a digital detox period.",
                accentColor = 0xFFD946EF,
            ),
        ),
        isLoading = false,
    )
}
