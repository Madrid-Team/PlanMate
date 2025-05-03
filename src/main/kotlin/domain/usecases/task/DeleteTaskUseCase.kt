package domain.usecases.task

import domain.repository.TaskRepository
import domain.utlis.TaskCannotDeleteException

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getTaskByIdUseCase: GetTaskByIdUseCase
) {
    fun deleteTask(taskId: String): Result<Unit> {
        try {
            val result = taskRepository.deleteTask(taskId)
            return if (result.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } catch (e: TaskCannotDeleteException) {
            return Result.failure(e)
        }
    }
}