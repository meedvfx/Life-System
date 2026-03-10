package com.meedz.lifeos.domain.repository_interfaces

import com.meedz.lifeos.domain.models.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun saveHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
}
