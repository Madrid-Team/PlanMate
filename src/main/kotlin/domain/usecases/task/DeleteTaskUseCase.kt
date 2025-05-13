package domain.usecases.task

import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
) {
    suspend fun deleteTask(taskId: String) {

        taskRepository.deleteTask(taskId)
    }
}