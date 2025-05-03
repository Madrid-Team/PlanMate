package data.repository

import data.mapper.toDto
import data.source.task.TaskDataSource
import domain.models.task.Task
import domain.repository.TaskRepository
import java.util.UUID

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TaskRepository {
    override fun editTask(task: Task): Boolean {
        return taskDataSource.editTask(task)
    }

    override fun deleteTask(taskId: String): Boolean {
        return taskDataSource.deleteTask(taskId)
    }

    override fun createTask(task: Task) {
        taskDataSource.createTask(task.toDto())
    }

    override fun getAllTasks(): List<Task> {
        return taskDataSource.getAllTasks()
    }

    override fun getTasksByProjectId(projectId: String): Result<List<Task>> {
         return taskDataSource.getTasksByProjectId(projectId)
    }

    override fun getTaskLogsByID(taskId: String): List<String> {
        return taskDataSource.getLogsByTaskId(taskId)
    }
}