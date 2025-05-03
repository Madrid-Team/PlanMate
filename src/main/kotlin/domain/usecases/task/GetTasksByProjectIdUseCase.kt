package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.utlis.TaskExceptions

class GetTasksByProjectIdUseCase(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(projectId: String): Result<List<Task>> {
        return try {
            val result = taskRepository.getTasksByProjectId(projectId)

            if (result.isSuccess) {
                val tasks = result.getOrNull()
                if (!tasks.isNullOrEmpty()) {
                    Result.success(tasks)
                } else {
                    Result.failure(TaskNotFoundException("No tasks found for this project."))
                }
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
