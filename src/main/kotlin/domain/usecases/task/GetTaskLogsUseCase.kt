package domain.usecases.task

import domain.repository.TaskRepository

class GetTaskLogsUseCase(
    private val taskRepository: TaskRepository
) {
    fun getTaskLogs(taskId: String): List<String> {
        return taskRepository.getTaskLogsByID(taskId)
    }

}