package com.meedz.lifeos.data.repository

import com.meedz.lifeos.data.dao.GoalDao
import com.meedz.lifeos.data.mappers.toDomain
import com.meedz.lifeos.data.mappers.toEntity
import com.meedz.lifeos.domain.models.Goal
import com.meedz.lifeos.domain.repository_interfaces.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {
    override fun getAllGoals(): Flow<List<Goal>> {
        return goalDao.getAllGoals().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveGoal(goal: Goal) {
        goalDao.insertGoal(goal.toEntity())
    }
}
