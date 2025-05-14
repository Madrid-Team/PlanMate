package domain.repository


import domain.models.task.Task

interface TaskRepository {
    suspend fun editTask(task: Task)
    suspend fun deleteTask(taskId: String)
    suspend fun createTask(task: Task)
    suspend fun getTasksByProjectId(projectId: String): List<Task>
    suspend fun getTaskLogsByTaskId(taskId: String): List<String>
}