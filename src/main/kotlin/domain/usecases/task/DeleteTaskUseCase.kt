package domain.usecases.task

import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
) {
    suspend fun deleteTask( taskId: String) {

        // Proceed to delete
        taskRepository.deleteTask(taskId)
    }
}