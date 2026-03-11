package com.meedz.lifeos.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meedz.lifeos.domain.models.Goal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class GoalsUiState(
    val activeGoals: List<Goal> = emptyList(),
    val completedGoals: List<Goal> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class GoalsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(GoalsUiState())
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()

    init {
        loadGoals()
    }

    private fun loadGoals() {
        _uiState.update { it.copy(isLoading = true) }
        // TODO: Replace with GoalRepository Flow Collection
        val mockActiveGoals = listOf(
            Goal(id = 1, title = "Master Jetpack Compose", description = "Build 5 complex UIs", targetDate = System.currentTimeMillis() + 86400000L * 30, progress = 45, isCompleted = false),
            Goal(id = 2, title = "Run a Marathon", description = "Complete 42km run", targetDate = System.currentTimeMillis() + 86400000L * 90, progress = 10, isCompleted = false)
        )
        _uiState.update { 
            it.copy(
                activeGoals = mockActiveGoals,
                isLoading = false
            ) 
        }
    }
}
