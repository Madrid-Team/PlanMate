package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository

class CreateTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun createTask(task: Task): Result<Unit> {
        return try {
            taskRepository.createTask(task)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}