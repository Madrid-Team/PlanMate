package domain.repository


import domain.models.task.Task

interface TaskRepository {
    suspend fun editTask(task: Task)
    suspend fun deleteTask(projectId: String,taskId: String)
    suspend fun createTask(task: Task)
    suspend fun getAllTasks(): List<Task>
    suspend fun getTasksByProjectId(projectId: String): List<Task>
    suspend fun getTaskLogsByID(projectId: String,taskId: String): List<String>
}