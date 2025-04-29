package domain.repository

import domain.models.task.Task

interface TaskRepository {
    fun editTask(taskId: String): Boolean

    fun getAllTasks(): List<Task>
}