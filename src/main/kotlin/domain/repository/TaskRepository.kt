package domain.repository

import domain.models.task.Task

interface TaskRepository {
    fun editTask(task: Task)
    fun deleteTask(taskId: String)
    fun createTask(task: Task)
    fun getAllTasks(): List<Task>
}