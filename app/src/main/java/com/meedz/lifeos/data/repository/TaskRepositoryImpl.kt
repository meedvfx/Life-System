package com.meedz.lifeos.data.repository

import com.meedz.lifeos.data.dao.TaskDao
import com.meedz.lifeos.data.mappers.toDomain
import com.meedz.lifeos.data.mappers.toEntity
import com.meedz.lifeos.domain.models.Task
import com.meedz.lifeos.domain.repository_interfaces.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities -> 
            entities.map { it.toDomain() } 
        }
    }

    override suspend fun saveTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }
}
