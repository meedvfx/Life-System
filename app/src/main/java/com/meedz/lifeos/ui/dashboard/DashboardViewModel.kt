package com.meedz.lifeos.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meedz.lifeos.domain.models.Habit
import com.meedz.lifeos.domain.models.Task
import com.meedz.lifeos.domain.usecases.habits.GetAllHabitsUseCase
import com.meedz.lifeos.domain.usecases.habits.UpdateHabitStreakUseCase
import com.meedz.lifeos.domain.usecases.tasks.GetTodayTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val todayTasks: List<Task> = emptyList(),
    val habits: List<Habit> = emptyList(),
    val productivityScore: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getTodayTasksUseCase: GetTodayTasksUseCase,
    getAllHabitsUseCase: GetAllHabitsUseCase,
    private val updateHabitStreakUseCase: UpdateHabitStreakUseCase
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        getTodayTasksUseCase(),
        getAllHabitsUseCase()
    ) { tasks, habits ->
        val score = calculateProductivityScore(tasks, habits)
        DashboardUiState(
            todayTasks = tasks,
            habits = habits,
            productivityScore = score,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    private fun calculateProductivityScore(tasks: List<Task>, habits: List<Habit>): Int {
        val completedTasks = tasks.count { it.isCompleted }
        val activeHabitStreaks = habits.sumOf { it.streak }
        return (completedTasks * 10) + (activeHabitStreaks * 5)
    }

    fun onHabitToggled(habit: Habit, isCompleted: Boolean) {
        viewModelScope.launch {
            updateHabitStreakUseCase(habit, isCompleted)
        }
    }
}
