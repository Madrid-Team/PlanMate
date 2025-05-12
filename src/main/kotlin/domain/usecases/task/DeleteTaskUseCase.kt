package domain.usecases.task

import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase
import domain.utils.TaskExceptions

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
) {
    suspend fun deleteTask(projectId: String, taskId: String) {
        getProjectByIdUseCase.getById(projectId)
        val tasks = getTasksByProjectIdUseCase.getTaskByProjectId(projectId)
        val taskExists = tasks.any { task ->
            task.id.toString() == taskId
        }
        if (!taskExists) {
            throw TaskExceptions.TaskNotFoundException()
        }
        taskRepository.deleteTask(projectId, taskId)
    }
}