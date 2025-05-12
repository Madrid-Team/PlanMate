package domain.usecases.task

import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase

class GetTaskLogsUseCase(
    private val taskRepository: TaskRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
) {
    suspend fun getTaskLogs(projectId: String, taskId: String): List<String> {
        // Verify project exists
        getProjectByIdUseCase.getById(projectId)

        // Verify task exists
        getTaskByIdUseCase.getTaskById(projectId, taskId)
        val logs = taskRepository.getTaskLogsByID(projectId, taskId)
        return logs
    }

}