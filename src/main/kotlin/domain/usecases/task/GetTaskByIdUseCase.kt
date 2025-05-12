package domain.usecases.task

import domain.models.task.Task
import domain.utils.TaskExceptions

class GetTaskByIdUseCase(
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase

) {
    suspend fun getTaskById(projectId: String, taskId: String): Task {
        val tasks = getTasksByProjectIdUseCase.getTaskByProjectId(projectId)
        val task = tasks.find { it.id.toString() == taskId }
        return task ?: throw TaskExceptions.TaskNotFoundException()
    }
}