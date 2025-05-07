package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository

class CreateTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun createTask(task: Task) {
        taskRepository.createTask(task)
    }
}