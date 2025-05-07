package org.madrid.data.source.task

import data.dto.task.TaskDto

interface RemoteTaskDataSource {
    suspend fun editTask(task: TaskDto)
    suspend fun deleteTask(projectId: String,taskId: String)
    suspend fun createTask(task: TaskDto)
    suspend fun getTasksByProjectId(projectId: String): List<TaskDto>
    suspend fun getTaskLogsByID(projectId: String,taskId: String): List<String>
}