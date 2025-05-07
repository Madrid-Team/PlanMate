package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository

class EditTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun editTask(task: Task) {
        taskRepository.editTask(task)
    }
}