package domain.repository

import domain.models.task.Task

interface TaskRepository {
    fun editTask(taskId: String, updatedTask: Task)
    fun deleteTask(taskId: String)
    fun createTask(task: Task): Boolean
    fun getAllTasks(): List<Task>
}