package com.meedz.lifeos.domain.usecases.habits

import com.meedz.lifeos.domain.models.Habit
import com.meedz.lifeos.domain.repository_interfaces.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    operator fun invoke(): Flow<List<Habit>> = habitRepository.getAllHabits()
}
