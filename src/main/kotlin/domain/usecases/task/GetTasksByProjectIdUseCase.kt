package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.utils.TaskExceptions

class GetTasksByProjectIdUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun getTaskByProjectId(projectId: String): List<Task> {
        return taskRepository.getTasksByProjectId(projectId).ifEmpty { throw TaskExceptions.TaskNotFoundException() }
    }
}
