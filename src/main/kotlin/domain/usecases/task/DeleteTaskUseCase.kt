package domain.usecases.task

import domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun deleteTask(taskId: String) {
        taskRepository.deleteTask(taskId)
    }
}