package data.source.task

import data.dto.task.TaskDto

interface TaskDataSource {
    fun editTask(tasks: List<TaskDto>)
    fun deleteTask(task: List<TaskDto>)
    fun createTask(task: TaskDto)
    fun getAllTasks(): List<TaskDto>
    fun getTasksByProjectId(projectId: String): List<TaskDto>
    fun getTaskLogsByID(taskId: String): List<String>
}