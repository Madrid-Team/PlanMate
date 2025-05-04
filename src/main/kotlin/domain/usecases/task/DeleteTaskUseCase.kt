package domain.usecases.task

import domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun deleteTask(taskId: String): Result<Unit> {
        try {
            val result = taskRepository.deleteTask(taskId)
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