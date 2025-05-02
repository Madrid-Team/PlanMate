package data.repository

import data.source.task.TaskDataSource
import domain.models.task.Task
import domain.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TaskRepository {
    override fun editTask(task: Task): Boolean {
        return taskDataSource.editTask(task)
    }

    override fun deleteTask(taskId: String): Boolean {
        return taskDataSource.deleteTask(taskId)
    }

    override fun createTask(task: Task): Boolean {
        return taskDataSource.createTask(task)
    }

    override fun getAllTasks(): List<Task> {
        return taskDataSource.getAllTasks()
    }

    override fun getTasksByProjectId(projectId: String): List<Task> {
        return taskDataSource.getTasksByProjectId(projectId)
    }

    override fun getTaskLogsBYID(taskId: String): List<String> {
        return taskDataSource.getLogsByTaskId(taskId)
    }
}