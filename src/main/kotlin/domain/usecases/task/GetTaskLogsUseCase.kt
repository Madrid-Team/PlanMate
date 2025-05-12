package domain.usecases.task

import domain.repository.TaskRepository

class GetTaskLogsUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: String): List<String> {
        return taskRepository.getTaskLogsByID(taskId)
    }

}