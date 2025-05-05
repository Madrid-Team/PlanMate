package domain.repository


import domain.models.task.Task

interface TaskRepository {
    fun editTask(task: Task)
    fun deleteTask(taskId: String)
    fun createTask(task: Task)
    fun getAllTasks(): List<Task>
    fun getTasksByProjectId(projectId: String): List<Task>
    fun getTaskLogsByID(taskId: String): Result<List<String>>
}