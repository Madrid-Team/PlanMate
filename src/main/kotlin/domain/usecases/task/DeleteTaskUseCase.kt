package domain.usecases.task

import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
) {
    suspend fun deleteTask(projectId: String, taskId: String) {
        // Verify project exists
        getProjectByIdUseCase.getById(projectId)

        // Verify task exists
        getTaskByIdUseCase.getTaskById(projectId, taskId)

        // Proceed to delete
        taskRepository.deleteTask(projectId, taskId)
    }
}