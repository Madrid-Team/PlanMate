package data.source.task

import data.dto.task.TaskDto
import domain.models.task.Task

interface TaskDataSource {
    fun editTask(tasks: List<TaskDto>): Result<Unit>
    fun deleteTask(tasks: List<TaskDto>): Result<Unit>
    fun createTask(task: TaskDto): Result<Unit>
    fun getAllTasks(): Result<List<TaskDto>>

//    fun getListWithDeletedTask(taskId: String): List<Task>
//    fun getListOfUpdatedList(task: Task): List<Task>
//    fun getTasksByProjectId(projectId: String): List<Task>
//    fun getLogsByTaskId(taskId: String): List<String>
}