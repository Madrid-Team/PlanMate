package data.source

import data.dto.task.TaskDto

interface TaskExternalDataSource {
    suspend fun editTask(task: TaskDto)
    suspend fun deleteTask(taskId: String)
    suspend fun createTask(task: TaskDto)
    suspend fun getTasksByProjectId(projectId: String): List<TaskDto>
    suspend fun getTaskLogsByID(taskId: String): List<String>
}
