package data.source.task

import data.dto.task.TaskDto
import domain.models.task.Task

interface TaskDataSource {
    fun editTask(tasks: List<TaskDto>): Result<Unit>

    fun deleteTask(taskId: String): Boolean
    fun createTask(task: Task): Boolean
    fun getAllTasks(): List<Task>
    fun getListWithDeletedTask(taskId: String): List<Task>
    fun getListOfUpdatedList(task: Task): List<Task>
    fun getTasksByProjectId(projectId: String): List<Task>
    fun getLogsByTaskId(taskId: String): List<String>
}