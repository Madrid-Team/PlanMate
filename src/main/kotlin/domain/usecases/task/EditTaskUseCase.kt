package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.usecases.task.validation.CheckTaskTitleValidationUseCase
import domain.utlis.TaskTitleInvalidException

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val taskTitleValidationUseCase: CheckTaskTitleValidationUseCase
) {
    fun editTask(task: Task): Result<Unit> {
        try {
            taskTitleValidationUseCase(task.title)
            val result = taskRepository.editTask(task)
            return if (result.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } catch (e: TaskTitleInvalidException) {
            return Result.failure(e)
        }
    }
}