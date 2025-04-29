package domain.usecases

import domain.repository.TaskRepository

class EditTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun editTask(taskId: String): Boolean {
        return false
    }
}