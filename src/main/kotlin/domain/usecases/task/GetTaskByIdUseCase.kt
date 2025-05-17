package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.utils.TaskExceptions

class GetTaskByIdUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun getTaskById(taskId: String): Task {
        if (taskId.isEmpty()) {
            throw TaskExceptions.TaskNotFoundException("Task Id is empty")
        }
        return taskRepository.getTaskById(taskId)
    }
}