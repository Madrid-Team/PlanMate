package domain.usecases.task

import domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: String) {
        taskRepository.deleteTask(taskId)
    }
}