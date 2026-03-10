package com.meedz.lifeos.domain.usecases.habits

import com.meedz.lifeos.domain.models.Habit
import com.meedz.lifeos.domain.repository_interfaces.HabitRepository
import java.util.Calendar
import javax.inject.Inject

class UpdateHabitStreakUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit, isCompletedToday: Boolean) {
        val today = Calendar.getInstance().timeInMillis
        val newStreak = if (isCompletedToday) habit.streak + 1 else 0
        val updatedHabit = habit.copy(
            streak = newStreak,
            lastCompletedDate = if (isCompletedToday) today else habit.lastCompletedDate
        )
        habitRepository.updateHabit(updatedHabit)
    }
}
