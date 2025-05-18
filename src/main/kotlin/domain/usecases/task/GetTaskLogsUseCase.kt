package domain.usecases.task

import domain.repository.TaskRepository
import domain.utils.NoLogsFoundException
import domain.utils.TaskExceptions

class GetTaskLogsUseCase(
    private val taskRepository: TaskRepository
) {
    suspend  fun getTaskLogsByTaskId(taskId: String): List<String> {
        return taskRepository.getTaskLogsByTaskId(taskId).ifEmpty { throw NoLogsFoundException() }
    }

}