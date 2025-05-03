package data.source.task

import data.dto.task.TaskDto
import domain.models.task.Task

interface TaskDataSource {
    fun editTask(task: Task): Boolean
    fun deleteTask(taskId: String): Boolean
    fun createTask(task: TaskDto)
    fun getAllTasks(): List<Task>
    fun getListWithDeletedTask(taskId: String): List<Task>
    fun getListOfUpdatedList(task: Task): List<Task>
    fun getTasksByProjectId(projectId: String): List<Task>
    fun getLogsByTaskId(taskId: String): List<String>
}