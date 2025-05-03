package domain.repository


import domain.models.task.Task

interface TaskRepository {
    fun editTask(task: Task): Boolean
    fun deleteTask(taskId: String): Boolean
    fun createTask(task: Task)
    fun getAllTasks(): List<Task>
    fun getTasksByProjectId(projectId: String): List<Task>
    fun getTaskLogsByID(taskId: String) : List<String>
}