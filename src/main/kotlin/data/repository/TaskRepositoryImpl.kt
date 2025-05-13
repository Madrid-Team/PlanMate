package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.task.TaskExternalDataSource
import data.utils.toTaskException
import domain.models.task.Task
import domain.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskExternalDataSource: TaskExternalDataSource,
) : TaskRepository {
    override suspend fun editTask(task: Task) = executeTaskOperation {
        taskExternalDataSource.editTask(task.toDto())
    }

    override suspend fun deleteTask(taskId: String) = executeTaskOperation {
        taskExternalDataSource.deleteTask(taskId)
    }

    override suspend fun createTask(task: Task) = executeTaskOperation {
        taskExternalDataSource.createTask(task.toDto())
    }

    override suspend fun getTasksByProjectId(projectId: String): List<Task> = executeTaskOperation {
        val tasks = taskExternalDataSource.getTasksByProjectId(projectId)
        tasks.map { task -> task.toDomain() }
    }

    override suspend fun getTaskLogsByID(taskId: String): List<String> = executeTaskOperation {
        taskExternalDataSource.getTaskLogsByID(taskId)
    }

    private suspend fun <T> executeTaskOperation(operation: suspend () -> T): T {
        return try {
            operation()
        } catch (e: Exception) {
            throw e.toTaskException()
        }
    }

}