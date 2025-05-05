package domain.repository


import domain.models.task.Task

interface TaskRepository {
    fun editTask(task: Task)
    fun deleteTask(taskId: String): Result<Unit>
    fun createTask(task: Task): Result<Unit>
    fun getAllTasks(): Result<List<Task>>
    fun getTasksByProjectId(projectId: String): Result<List<Task>>
    fun getTaskLogsByID(taskId: String): Result<List<String>>
}