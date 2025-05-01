package data.source.task

import domain.models.task.Task

interface TaskDataSource {
    fun editTask(task: Task): Result<Unit>
    fun deleteTask(taskId: String): Result<Unit>
    fun createTask(task: Task): Result<Unit>
    fun getAllTasks(): List<Task>
}