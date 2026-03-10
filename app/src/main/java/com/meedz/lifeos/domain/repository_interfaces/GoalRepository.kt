package com.meedz.lifeos.domain.repository_interfaces

import com.meedz.lifeos.domain.models.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getAllGoals(): Flow<List<Goal>>
    suspend fun saveGoal(goal: Goal)
}
