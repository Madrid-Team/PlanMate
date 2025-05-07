package domain.usecases.task

import domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun deleteTask(projectId: String,taskId: String) {
        taskRepository.deleteTask(projectId,taskId)
    }
}