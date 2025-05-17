package domain.usecases.task

import domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
) {
    suspend fun deleteTaskByTaskId(taskId: String) {

        taskRepository.deleteTaskByTaskId(taskId)
    }
}