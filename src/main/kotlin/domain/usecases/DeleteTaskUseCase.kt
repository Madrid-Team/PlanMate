package domain.usecases

import domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun deleteTask(taskId: String): Boolean {
        return false
    }
}