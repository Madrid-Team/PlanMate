package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.usecases.task.validation.CheckTaskValidationUseCase
import domain.utlis.CannotCreateTaskException

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val checkTaskValidationUseCase: CheckTaskValidationUseCase
) {
    fun createTask(task: Task): Result<Unit> {
        try {
            checkTaskValidationUseCase(task)
            val result = taskRepository.createTask(task)
            return if (result.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } catch (e: CannotCreateTaskException) {
            return Result.failure(e)
        }
    }
}