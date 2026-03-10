package com.meedz.lifeos.data.repository

import com.meedz.lifeos.data.dao.HabitDao
import com.meedz.lifeos.data.mappers.toDomain
import com.meedz.lifeos.data.mappers.toEntity
import com.meedz.lifeos.domain.models.Habit
import com.meedz.lifeos.domain.repository_interfaces.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {
    override fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveHabit(habit: Habit) {
        habitDao.insertHabit(habit.toEntity())
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit.toEntity())
    }
}
