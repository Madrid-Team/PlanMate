package data.source.task

import data.dto.task.TaskDto

interface TaskDataSource {
    fun editTask(tasks: List<TaskDto>)
    fun deleteTask(task: List<TaskDto>): Result<Unit>
    fun createTask(task: TaskDto):Result<Unit>
    fun getAllTasks(): Result<List<TaskDto>>
    fun getTasksByProjectId(projectId: String): Result<List<TaskDto>>
}