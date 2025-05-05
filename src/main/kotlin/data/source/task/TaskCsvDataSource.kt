package data.source.task

import data.dto.task.TaskDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.taskHeader
import domain.utlis.TaskExceptions

class TaskCsvDataSource(
    private val taskCsvParser: TaskCsvParser,
    private val fileCsvWriter: FileCsvWriter,
    private val fileCsvReader: FileCsvReader
) : TaskDataSource {
    override fun editTask(tasks: List<TaskDto>) {
        var taskAfterUpdate = String.taskHeader
        tasks.forEach {
            val taskAsString = taskCsvParser.parseTaskToString(it)
            taskAfterUpdate += taskAsString
            fileCsvWriter.updateCsvFile(taskAfterUpdate)
        }
    }

    override fun deleteTask(task: List<TaskDto>) {
        var tasksFileContentAfterDeletion = String.taskHeader
        task.forEach {
            val projectAsString = taskCsvParser.parseTaskToString(it)
            tasksFileContentAfterDeletion += projectAsString
        }
        fileCsvWriter.updateCsvFile(tasksFileContentAfterDeletion)
    }

    override fun createTask(task: TaskDto) {
        val taskRow = taskCsvParser.parseTaskToString(task)
        fileCsvWriter.writeToCsvFile(taskRow)
    }

    override fun getAllTasks(): Result<List<TaskDto>> {
        val tasks = mutableListOf<TaskDto>()

        return try {

            fileCsvReader.readCsvFile().forEach { row ->
                if (row.isNotEmpty()) {
                    tasks.add(taskCsvParser.parseOneRowToTask(row))
                }
            }

            Result.success(tasks)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getTasksByProjectId(projectId: String): Result<List<TaskDto>> {
        val allTasks = getAllTasks().getOrNull()
        return if (!allTasks.isNullOrEmpty()) {
            Result.success(allTasks)
        } else {
            Result.failure(TaskExceptions.TaskNotFoundException("Tasks not found"))
        }
    }
}