package data.repository

import data.dto.task.TaskDto
import data.mapper.toDomain
import data.mapper.toDto
import data.source.task.TaskDataSource
import data.source.task.TaskMemoryDataSource
import domain.models.project.Project
import domain.models.task.Task
import domain.repository.TaskRepository
import domain.utlis.PlanMateExceptions
import domain.utlis.ProjectExceptions
import domain.utlis.TaskExceptions
import java.io.FileNotFoundException
import java.io.IOException

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val taskMemoryDataSource: TaskMemoryDataSource,
) : TaskRepository {

    init {
        initTasks()
    }

    private fun initTasks() {
        val result = taskDataSource.getAllTasks()
        if (result.isSuccess) {
            val dtoList = result.getOrNull() as? List<TaskDto> ?: emptyList()
            val a = dtoList.map { it.toDomain() }.toMutableList()
            taskMemoryDataSource.setTasks(a)
        } else {
            mutableListOf<Project>()
        }

    }

    override fun getAllTasks(): Result<List<Task>> {
        val allTasks = taskMemoryDataSource.getTasks()
        return if (allTasks.isNotEmpty()) {
            Result.success(allTasks)
        } else {
            Result.failure(PlanMateExceptions("You haven't any projects yet"))
        }
    }

    override fun createTask(task: Task): Result<Unit> {
        val result = taskDataSource.createTask(task.toDto())

        return if (result.isSuccess) {
            taskMemoryDataSource.addTask(task)
            result
        } else {
            when (val exception = result.exceptionOrNull()) {
                is FileNotFoundException -> Result.failure(ProjectExceptions.ProjectsFileNotExistsException())
                is IOException -> Result.failure(ProjectExceptions.ProjectsReadWriteException())
                else -> {
                    Result.failure(PlanMateExceptions(exception?.message.toString()))
                }
            }
        }
    }

    override fun editTask(task: Task): Result<Unit> {
        val taskListAfterUpdateProject = taskMemoryDataSource.editTask(task)

        val result = taskDataSource.editTask(taskListAfterUpdateProject.map { it.toDto() })

        return if (result.isSuccess) {
            Result.success(Unit)
        } else {
            taskMemoryDataSource.addTask(task)
            Result.failure(result.exceptionOrNull() ?: PlanMateExceptions("Failed to edit project"))
        }
    }

    override fun deleteTask(taskId: String): Result<Unit> {
        val taskListAfterUpdateProject = taskMemoryDataSource.deleteTask(taskId)

        val result = taskDataSource.deleteTask(taskListAfterUpdateProject.map { it.toDto() })

        return if (result.isSuccess) {
            Result.success(Unit)
        } else {
            Result.failure(result.exceptionOrNull() ?: PlanMateExceptions("Failed to delete project"))
        }

    }


    override fun getTasksByProjectId(projectId: String): Result<List<Task>> {
        val allTasks = taskMemoryDataSource.getTasks().filter { it.projectId == projectId }
        return if (allTasks.isNotEmpty()) {
            Result.success(allTasks)
        } else Result.failure(TaskExceptions.TaskNotFoundException("You haven't any projects yet"))
    }

    override fun getTaskLogsByID(taskId: String): Result<List<String>> {
        val task = taskMemoryDataSource.getTasks().find { it.id.toString() == taskId }
        return if (task != null) {
            Result.success(task.logs)
        } else {
            Result.failure(ProjectExceptions.ProjectNotFoundException())
        }
    }

}