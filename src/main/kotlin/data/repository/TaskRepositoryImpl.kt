package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.task.ExternalTaskDataSource
import data.utils.toTaskException
import domain.models.task.Task
import domain.repository.TaskRepository

class TaskRepositoryImpl(
    private val externalTaskDataSource: ExternalTaskDataSource,
) : TaskRepository{
    override suspend fun editTask(task: Task) {
        return try {
            externalTaskDataSource.editTask(task.toDto())
        }catch (e: Exception){
            throw e.toTaskException()
        }
    }

    override suspend fun deleteTask(projectId: String,taskId: String) {
        return try {
            externalTaskDataSource.deleteTask(projectId,taskId)
        }catch (e:Exception){
            throw e.toTaskException()
        }
    }

    override suspend fun createTask(task: Task) {
        return try {
            externalTaskDataSource.createTask(task.toDto())
        }catch (e:Exception){
            throw e.toTaskException()
        }
    }

    override suspend fun getTasksByProjectId(projectId: String): List<Task> {
        return try {
            externalTaskDataSource.getTasksByProjectId(projectId).map { it.toDomain() }
        }catch (e:Exception){
            throw e.toTaskException()
        }
    }

    override suspend fun getTaskLogsByID(projectId: String,taskId: String): List<String> {
        return try {
            externalTaskDataSource.getTaskLogsByID(projectId , taskId)
        }catch (e:Exception){
            throw e.toTaskException()
        }
    }
}