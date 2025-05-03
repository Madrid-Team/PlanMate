package data.repository

import data.mapper.toDto
import data.source.task.TaskDataSource
import data.source.task.TaskMemoryDataSource
import domain.models.task.Task
import domain.repository.TaskRepository
import domain.utlis.TaskCannotEditException

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val taskMemoryDataSource: TaskMemoryDataSource
) : TaskRepository {
    init {
        initTasks()
    }

    private fun initTasks() {
        val result = taskDataSource.getAllTasks()
        taskMemoryDataSource.initializeTasks(result)
    }

    override fun editTask(task: Task): Result<Unit> {
        val tasksListAfterUpdates = taskMemoryDataSource.editTask(task)

        val result = taskDataSource.editTask(tasksListAfterUpdates.map { it.toDto() })

        return if (result.isSuccess) {
            Result.success(Unit)
        } else {
            taskMemoryDataSource.addTaskToMemory(task)
            Result.failure(result.exceptionOrNull() ?: TaskCannotEditException())
        }
    }

    override fun deleteTask(taskId: String): Boolean {
        return taskDataSource.deleteTask(taskId)
    }

    override fun createTask(task: Task): Boolean {
        return taskDataSource.createTask(task)
    }

    override fun getAllTasks(): List<Task> {
        return taskDataSource.getAllTasks()
    }

    override fun getTasksByProjectId(projectId: String): List<Task> {
        return taskDataSource.getTasksByProjectId(projectId)
    }

    override fun getTaskLogsByID(taskId: String): List<String> {
        return taskDataSource.getLogsByTaskId(taskId)
    }
}