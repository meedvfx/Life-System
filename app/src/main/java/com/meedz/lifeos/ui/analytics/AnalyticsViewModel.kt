package com.meedz.lifeos.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meedz.lifeos.domain.models.ProductivityMetrics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AnalyticsUiState(
    val weeklyMetrics: List<ProductivityMetrics> = emptyList(),
    val averageScore: Int = 0,
    val isLoading: Boolean = false
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadMetrics()
    }

    private fun loadMetrics() {
        _uiState.update { it.copy(isLoading = true) }
        
        val baseTime = System.currentTimeMillis()
        val mockMetrics = (0..6).map { i ->
            ProductivityMetrics(
                dateMillis = baseTime - (i * 86400000L),
                taskCompletionRate = (50..100).random() / 100f,
                habitAdherenceRate = (30..100).random() / 100f,
                overallScore = (60..95).random()
            )
        }.reversed()
        
        val avg = if (mockMetrics.isNotEmpty()) mockMetrics.sumOf { it.overallScore } / mockMetrics.size else 0

        _uiState.update { 
            it.copy(
                weeklyMetrics = mockMetrics,
                averageScore = avg,
                isLoading = false
            ) 
        }
    }
}
