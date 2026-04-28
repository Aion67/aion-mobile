package com.example.enaf.ui.screens.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.repository.LocalRepository
import com.example.enaf.ui.components.toUiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InsightsViewModel(
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState())
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null

    init {
        observeActiveSession()
    }

    fun onEvent(event: InsightsUiEvent) {
        when (event) {
            is InsightsUiEvent.RangeSelected -> {
                _uiState.value = _uiState.value.copy(selectedRange = event.range)
                refresh()
            }
            InsightsUiEvent.Refresh -> refresh()
        }
    }

    private fun observeActiveSession() {
        viewModelScope.launch {
            localRepository.observeActiveSession().collect { session ->
                val userId = session?.userId
                if (userId == null) {
                    _uiState.value = InsightsUiState(
                        observations = listOf(
                            InsightsObservationUiModel(
                                title = "No Active Profile",
                                description = "Start a guest session to compute your insight patterns.",
                                accentColor = 0xFF007BFF,
                            )
                        ),
                        isLoading = false,
                    )
                    return@collect
                }

                currentUserId = userId
                loadInsightsState(userId)
            }
        }
    }

    private fun refresh() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            loadInsightsState(userId)
        }
    }

    private suspend fun loadInsightsState(userId: String) {
        try {
            val summaries = localRepository.getDailySummaries(userId)
            val scoped = scopedSummaries(summaries)
            val streak = scoped.maxOfOrNull { it.streakDay } ?: 0

            val totalFocus = scoped.sumOf { it.focusMinutes }.coerceAtLeast(1)
            val totalScroll = scoped.sumOf { it.scrollMinutes }
            val totalSaved = scoped.sumOf { it.timeSavedMinutes }
            val recovery = (totalSaved / 2).coerceAtLeast(0)
            val bucketTotal = (totalFocus + totalScroll + totalSaved + recovery).coerceAtLeast(1)

            val categories = listOf(
                InsightsCategoryUiModel("Focus", percentLabel(totalFocus, bucketTotal), 0xFF007BFF),
                InsightsCategoryUiModel("Wellness", percentLabel(totalSaved, bucketTotal), 0xFFD946EF),
                InsightsCategoryUiModel("Productivity", percentLabel(recovery, bucketTotal), 0xFF00F2EA),
                InsightsCategoryUiModel("Social", percentLabel(totalScroll, bucketTotal), 0xFFF97316),
            )

            _uiState.value = InsightsUiState(
                selectedRange = _uiState.value.selectedRange,
                streakDays = streak,
                totalDaysLabel = "$streak Days Total",
                categories = categories,
                observations = buildObservations(scoped),
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

    private fun scopedSummaries(summaries: List<com.example.enaf.data.local.entity.DailySummaryEntity>): List<com.example.enaf.data.local.entity.DailySummaryEntity> {
        return when (_uiState.value.selectedRange) {
            InsightsRange.WEEKLY -> summaries.take(7)
            InsightsRange.MONTHLY -> summaries.take(30)
            InsightsRange.YEARLY -> summaries
        }
    }

    private fun buildObservations(
        summaries: List<com.example.enaf.data.local.entity.DailySummaryEntity>,
    ): List<InsightsObservationUiModel> {
        if (summaries.isEmpty()) {
            return listOf(
                InsightsObservationUiModel(
                    title = "No Data Yet",
                    description = "Use planner limits for a day and your patterns will appear here.",
                    accentColor = 0xFF007BFF,
                )
            )
        }

        val avgFocus = summaries.map { it.focusMinutes }.average().toInt()
        val avgScroll = summaries.map { it.scrollMinutes }.average().toInt()

        return listOf(
            InsightsObservationUiModel(
                title = "Focus Rhythm",
                description = "Average focus is ${avgFocus}m/day. Keep this above 180m to accelerate level gains.",
                accentColor = 0xFF007BFF,
            ),
            InsightsObservationUiModel(
                title = "Scroll Pressure",
                description = "Average social scroll is ${avgScroll}m/day. Cut 20m to improve credit consistency.",
                accentColor = 0xFFD946EF,
            ),
        )
    }

    private fun percentLabel(value: Int, total: Int): String {
        val percentage = ((value.toDouble() / total.toDouble()) * 100.0).toInt().coerceIn(0, 100)
        return "$percentage%"
    }
}
