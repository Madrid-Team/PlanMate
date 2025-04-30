package domain.usecases

import domain.models.task.Task
import domain.repository.TaskRepository

class EditTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun editTask(task: Task): Boolean {
        return true
    }
}