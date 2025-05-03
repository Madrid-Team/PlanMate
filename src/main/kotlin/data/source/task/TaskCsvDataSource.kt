package data.source.task

import data.dto.task.TaskDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.taskHeader
import domain.models.logs.EntityType
import domain.models.logs.UpdatedLogFormatter
import domain.models.task.Task
import domain.utlis.TaskExceptions
import domain.utlis.convertDateIntoReadableDate
import java.time.LocalDateTime

class TaskCsvDataSource(
    private val taskCsvParser: TaskCsvParser,
    private val fileCsvWriter: FileCsvWriter,
    private val fileCsvReader: FileCsvReader
) : TaskDataSource {
//    override fun editTask(task: Task): Boolean {
//        return try {
//            val updatedTasks = getListOfUpdatedList(task)
//            val csvContent = updatedTasks.joinToString("\n") { taskCsvParser.parseTaskToString(it.toDto()) }
//            fileCsvWriter.updateCsvFile(csvContent)
//            true
//        } catch (e: Exception) {
//            throw TaskNotFoundException(e.message)
//        }
//    }
//
//    override fun deleteTask(taskId: String): Boolean {
//        return try {
//            val listOfUpdatedTasks = getListWithDeletedTask(taskId)
//            val csvContent = listOfUpdatedTasks.joinToString("\n") {
//                taskCsvParser.parseTaskToString(it.toDto())
//            }
//            fileCsvWriter.updateCsvFile(csvContent)
//            true
//        } catch (e: Exception) {
//            throw TaskNotFoundException(e.message)
//        }
//    }

    override fun editTask(tasks: List<TaskDto>): Result<Unit> {
        return try {
            var taskAfterUpdate = String.taskHeader
            tasks.forEach {
                val projectAsString = taskCsvParser.parseTaskToString(it)
                taskAfterUpdate += projectAsString
            }
            fileCsvWriter.updateCsvFile(taskAfterUpdate)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun deleteTask(task: List<TaskDto>): Result<Unit> {
        return try {
            var tasksFileContentAfterDeletion = String.taskHeader
            task.forEach {
                val projectAsString = taskCsvParser.parseTaskToString(it)
                tasksFileContentAfterDeletion += projectAsString
            }
            fileCsvWriter.updateCsvFile(tasksFileContentAfterDeletion)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun createTask(task: TaskDto): Result<Unit> {
        return try {
            val taskRow = taskCsvParser.parseTaskToString(task)
            fileCsvWriter.writeToCsvFile(taskRow)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
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

    override fun getLogsByTaskId(taskId: String): Result<List<String>> {

        val task = getAllTasks().getOrNull()?.find { it.id.toString() == taskId } ?: throw TaskExceptions.TaskNotFoundException()
        val taskLogs = task.logs
        if (taskLogs.isEmpty())
            throw TaskExceptions.NoLogsFoundException()
        return Result.success(taskLogs)
    }
    private fun createLogsForUpdatedFields(oldTask: Task, updatedTask: Task): List<String> {
        val logs = mutableListOf<String>()
        val timestamp = LocalDateTime.now().convertDateIntoReadableDate()
        if (oldTask.title != updatedTask.title) {
            logs.add(
                //create log message contains the update on project name
                UpdatedLogFormatter.format(
                    entityName = oldTask.title,
                    entityType = EntityType.TASK,
                    username = oldTask.createdBy,
                    fieldName = "title",
                    oldValue = oldTask.title,
                    newValue = updatedTask.title,
                    timestamp = timestamp
                )
            )
        }
        if (oldTask.description != updatedTask.description) {
            logs.add(
                //create log message contains the update on project name
                UpdatedLogFormatter.format(
                    entityName = oldTask.title,
                    entityType = EntityType.TASK,
                    username = oldTask.createdBy,
                    fieldName = "description",
                    oldValue = oldTask.description,
                    newValue = updatedTask.description,
                    timestamp = timestamp
                )
            )
        }

        if (oldTask.taskState != updatedTask.taskState) {
            logs.add(
                //create log message contains the update on project name
                UpdatedLogFormatter.format(
                    entityName = oldTask.title,
                    entityType = EntityType.TASK,
                    username = oldTask.createdBy,
                    fieldName = "state",
                    oldValue = oldTask.taskState,
                    newValue = updatedTask.taskState,
                    timestamp = timestamp
                )
            )
        }

        return logs
    }

}