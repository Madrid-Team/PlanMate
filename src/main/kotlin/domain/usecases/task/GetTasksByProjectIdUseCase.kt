package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository

class GetTasksByProjectIdUseCase(
    private val taskRepository: TaskRepository
) {
    suspend  fun getTaskByProjectId(projectId: String): List<Task> {
        return taskRepository.getTasksByProjectId(projectId)
    }
}
