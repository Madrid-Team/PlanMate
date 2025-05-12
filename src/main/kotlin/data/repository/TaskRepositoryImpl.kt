package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.task.TaskExternalDataSource
import data.utils.toTaskException
import domain.models.task.Task
import domain.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskExternalDataSource: TaskExternalDataSource,
) : TaskRepository{
    override suspend fun editTask(task: Task) {
        return try {
            taskExternalDataSource.editTask(task.toDto())
        }catch (e: Exception){
            throw e.toTaskException()
        }
    }

    override suspend fun deleteTask(taskId: String) {
        return try {
            taskExternalDataSource.deleteTask(taskId)
        }catch (e:Exception){
            throw e.toTaskException()
        }
    }

    override suspend fun createTask(task: Task) {
        return try {
            taskExternalDataSource.createTask(task.toDto())
        }catch (e:Exception){
            throw e.toTaskException()
        }
    }

    override suspend fun getTasksByProjectId(projectId: String): List<Task> {
        return try {
            taskExternalDataSource.getTasksByProjectId(projectId).map { it.toDomain() }
        }catch (e:Exception){
            throw e.toTaskException()
        }
    }

    override suspend fun getTaskLogsByID(taskId: String): List<String> {
        return try {
            taskExternalDataSource.getTaskLogsByID(taskId)
        }catch (e:Exception){
            throw e.toTaskException()
        }
    }
}