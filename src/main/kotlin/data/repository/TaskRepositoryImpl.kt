package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import domain.models.task.Task
import domain.repository.TaskRepository
import org.madrid.data.source.task.RemoteTaskDataSource

class TaskRepositoryImpl(
    private val taskDataSource: RemoteTaskDataSource,
) : TaskRepository{
    override suspend fun editTask(task: Task) {
        return taskDataSource.editTask(task.toDto())
    }

    override suspend fun deleteTask(projectId: String,taskId: String) {
        return taskDataSource.deleteTask(projectId,taskId)
    }

    override suspend fun createTask(task: Task) {
        return taskDataSource.createTask(task.toDto())
    }

    override suspend fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksByProjectId(projectId: String): List<Task> {
        return taskDataSource.getTasksByProjectId(projectId).map { it.toDomain() }
    }

    override suspend fun getTaskLogsByID(projectId: String,taskId: String): List<String> {
        return taskDataSource.getTaskLogsByID(projectId , taskId)
    }
}