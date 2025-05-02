package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.utlis.TaskNotFoundException

class GetTasksByProjectIdUseCase(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(projectId: String): List<Task> =
        taskRepository.getTasksByProjectId(projectId).takeIf { it.isNotEmpty() } ?: throw TaskNotFoundException()
}
