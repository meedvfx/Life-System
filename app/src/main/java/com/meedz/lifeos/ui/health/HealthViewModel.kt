package com.meedz.lifeos.ui.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meedz.lifeos.domain.models.HealthMetric
import com.meedz.lifeos.domain.models.HealthMetricType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class HealthUiState(
    val recentMetrics: List<HealthMetric> = emptyList(),
    val averageSleep: Double = 0.0,
    val latestWeight: Double = 0.0,
    val isLoading: Boolean = false
)

@HiltViewModel
class HealthViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HealthUiState())
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()

    init {
        loadHealthData()
    }

    private fun loadHealthData() {
        _uiState.update { it.copy(isLoading = true) }
        
        val mockMetrics = listOf(
            HealthMetric(id = 1, type = HealthMetricType.SLEEP_HOURS, value = 7.5, unit = "hrs"),
            HealthMetric(id = 2, type = HealthMetricType.WEIGHT, value = 75.2, unit = "kg"),
            HealthMetric(id = 3, type = HealthMetricType.WORKOUT_MINUTES, value = 45.0, unit = "mins"),
            HealthMetric(id = 4, type = HealthMetricType.WATER_GLASSES, value = 6.0, unit = "glasses")
        )

        val avgSleep = mockMetrics.filter { it.type == HealthMetricType.SLEEP_HOURS }.map { it.value }.average().takeIf { !it.isNaN() } ?: 0.0
        val lWeight = mockMetrics.filter { it.type == HealthMetricType.WEIGHT }.maxByOrNull { it.dateMillis }?.value ?: 0.0
        
        _uiState.update { 
            it.copy(
                recentMetrics = mockMetrics,
                averageSleep = avgSleep,
                latestWeight = lWeight,
                isLoading = false
            ) 
        }
    }
}
