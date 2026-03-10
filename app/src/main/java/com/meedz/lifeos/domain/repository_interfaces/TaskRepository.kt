package com.meedz.lifeos.domain.repository_interfaces

import com.meedz.lifeos.domain.models.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun saveTask(task: Task)
}
