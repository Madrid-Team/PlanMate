package domain.usecases.task

import domain.repository.TaskRepository

class GetTaskLogsUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(projectId: String,taskId: String): List<String> {
        return taskRepository.getTaskLogsByID(projectId,taskId)
    }

}