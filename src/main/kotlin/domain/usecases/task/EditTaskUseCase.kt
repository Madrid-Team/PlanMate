package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository

class EditTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun editTask(task: Task): Result<Unit> {
        try {
            val result = taskRepository.editTask(task)
            return if (result.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}