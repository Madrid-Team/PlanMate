package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.task.TaskDataSource
import data.source.task.TaskMemoryDataSource
import data.utils.toTaskException
import domain.models.project.Project
import domain.models.task.Task
import domain.repository.TaskRepository
import domain.utlis.TaskExceptions

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val taskMemoryDataSource: TaskMemoryDataSource,
) : TaskRepository {

    init {
        initTasks()
    }

    private fun initTasks() {
        val tasksFromDataSource = taskDataSource.getAllTasks()
        if (tasksFromDataSource.isNotEmpty()) {
            val domainTasks = tasksFromDataSource.map { it.toDomain() }.toMutableList()
            taskMemoryDataSource.setTasks(domainTasks)
        } else {
            mutableListOf<Project>()
        }
    }

    override fun getAllTasks(): List<Task> {
        val allTasks = taskMemoryDataSource.getTasks()
        return allTasks.ifEmpty {
            throw TaskExceptions.TaskNotFoundException("Can't get tasks")
        }
    }

    override fun createTask(task: Task) {
        try {
            taskDataSource.createTask(task.toDto())
            taskMemoryDataSource.addTask(task)
        } catch (exception: Exception) {
            throw exception.toTaskException()
        }
    }

    override fun editTask(task: Task) {
        try {
            val taskListAfterUpdateProject = taskMemoryDataSource.editTask(task)
            taskDataSource.editTask(taskListAfterUpdateProject.map { it.toDto() })
        } catch (exception: Exception) {
            taskMemoryDataSource.addTask(task)
            throw exception.toTaskException()
        }
    }

    override fun deleteTask(taskId: String) {
        try {
            val taskListAfterUpdateProject = taskMemoryDataSource.deleteTask(taskId)
            taskDataSource.deleteTask(taskListAfterUpdateProject.map { it.toDto() })
        } catch (exception: Exception) {
            throw exception.toTaskException()
        }
    }

    override fun getTasksByProjectId(projectId: String): List<Task> {
        val allTasks = taskMemoryDataSource.getTasks().filter { it.projectId == projectId }
        return allTasks.ifEmpty {
            throw TaskExceptions.TaskNotFoundException("Can't get tasks for this project")
        }
    }

    override fun getTaskLogsByID(taskId: String): List<String> {
        return taskMemoryDataSource.getTaskLogsById(taskId).ifEmpty {
            throw TaskExceptions.NoLogsFoundException()
        }
    }
}