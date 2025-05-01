package data.repository

import data.source.task.TaskDataSource
import domain.models.task.Task
import domain.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TaskRepository {
    override fun editTask(task: Task): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun createTask(task: Task): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }
}