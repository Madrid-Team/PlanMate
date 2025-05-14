package domain.usecases.task

import domain.repository.TaskRepository
import domain.utils.TaskExceptions

class GetTaskLogsUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: String): List<String> {
        return taskRepository.getTaskLogsByTaskId(taskId).ifEmpty { throw TaskExceptions.NoLogsFoundException() }
    }

}