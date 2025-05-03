package domain.repository


import domain.models.task.Task

interface TaskRepository {
    fun editTask(task: Task): Result<Unit>

    fun deleteTask(taskId: String): Boolean
    fun createTask(task: Task): Boolean
    fun getAllTasks(): List<Task>
    fun getTasksByProjectId(projectId: String): List<Task>
    fun getTaskLogsByID(taskId: String): List<String>
}