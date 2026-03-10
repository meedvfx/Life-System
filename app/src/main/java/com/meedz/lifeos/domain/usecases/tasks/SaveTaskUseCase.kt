package com.meedz.lifeos.domain.usecases.tasks

import com.meedz.lifeos.domain.models.Task
import com.meedz.lifeos.domain.repository_interfaces.TaskRepository
import javax.inject.Inject

class SaveTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        taskRepository.saveTask(task)
    }
}
