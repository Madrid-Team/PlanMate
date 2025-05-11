package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository

class EditTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        taskRepository.editTask(task)
    }
}