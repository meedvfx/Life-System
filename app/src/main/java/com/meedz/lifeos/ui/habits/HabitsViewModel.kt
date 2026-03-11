package com.meedz.lifeos.ui.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meedz.lifeos.domain.models.Habit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class HabitsUiState(
    val activeHabits: List<Habit> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class HabitsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HabitsUiState())
    val uiState: StateFlow<HabitsUiState> = _uiState.asStateFlow()

    init {
        loadHabits()
    }

    private fun loadHabits() {
        _uiState.update { it.copy(isLoading = true) }
        val mockHabits = listOf(
            Habit(id = 1, name = "Morning Meditation", description = "10 minutes of mindfulness", frequency = "Daily", streak = 14, lastCompletedDate = System.currentTimeMillis()),
            Habit(id = 2, name = "Read 20 Pages", description = "Evening reading sessions", frequency = "Daily", streak = 5, lastCompletedDate = System.currentTimeMillis() - 86400000L),
            Habit(id = 3, name = "Gym Workout", description = "Weight lifting routine", frequency = "Mon, Wed, Fri", streak = 0, lastCompletedDate = null)
        )
        _uiState.update { 
            it.copy(
                activeHabits = mockHabits,
                isLoading = false
            ) 
        }
    }
    
    fun toggleHabit(habit: Habit, isCompleted: Boolean) {
        // Implement completion toggle logic and streak incrementation
    }
}
