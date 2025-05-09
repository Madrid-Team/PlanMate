package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.task.ExternalTaskDataSource
import domain.models.task.Task
import domain.repository.TaskRepository

class TaskRepositoryImpl(
    private val externalTaskDataSource: ExternalTaskDataSource,
) : TaskRepository{
    override suspend fun editTask(task: Task) {
        return externalTaskDataSource.editTask(task.toDto())
    }

    override suspend fun deleteTask(projectId: String,taskId: String) {
        return externalTaskDataSource.deleteTask(projectId,taskId)
    }

    override suspend fun createTask(task: Task) {
        return externalTaskDataSource.createTask(task.toDto())
    }

    override suspend fun getTasksByProjectId(projectId: String): List<Task> {
        return externalTaskDataSource.getTasksByProjectId(projectId).map { it.toDomain() }
    }

    override suspend fun getTaskLogsByID(projectId: String,taskId: String): List<String> {
        return externalTaskDataSource.getTaskLogsByID(projectId , taskId)
    }
}