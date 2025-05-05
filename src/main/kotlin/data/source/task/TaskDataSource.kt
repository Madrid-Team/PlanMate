package data.source.task

import data.dto.task.TaskDto

interface TaskDataSource {
    fun editTask(tasks: List<TaskDto>)
    fun deleteTask(task: List<TaskDto>)
    fun createTask(task: TaskDto)
    fun getAllTasks(): Result<List<TaskDto>>
    fun getTasksByProjectId(projectId: String): Result<List<TaskDto>>
}