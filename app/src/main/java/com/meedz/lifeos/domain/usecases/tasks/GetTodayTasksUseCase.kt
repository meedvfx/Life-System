package com.meedz.lifeos.domain.usecases.tasks

import com.meedz.lifeos.domain.models.Task
import com.meedz.lifeos.domain.repository_interfaces.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class GetTodayTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val todayEnd = todayStart + (24 * 60 * 60 * 1000)

        return taskRepository.getAllTasks().map { tasks ->
            tasks.filter { it.dueDate != null && it.dueDate >= todayStart && it.dueDate < todayEnd }
        }
    }
}
