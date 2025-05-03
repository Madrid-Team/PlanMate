package data.source.task

import data.mapper.toDomain
import data.mapper.toDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.toTaskException
import domain.models.logs.CreatedLogFormatter
import domain.models.logs.EntityType
import domain.models.logs.UpdatedLogFormatter
import domain.models.task.Task
import domain.utlis.TaskExceptions

import domain.utlis.convertDateIntoReadableDate
import java.io.IOException
import java.time.LocalDateTime

class TaskCsvDataSource(
    private val taskCsvParser: TaskCsvParser,
    private val fileCsvWriter: FileCsvWriter,
    private val fileCsvReader: FileCsvReader
) : TaskDataSource {
    override fun editTask(task: Task): Boolean {
        return try {
            val updatedTasks = getListOfUpdatedList(task)
            val csvContent = updatedTasks.joinToString("\n") { taskCsvParser.parseTaskToString(it.toDto()) }
            fileCsvWriter.updateCsvFile(csvContent)
            true
        } catch (e: Exception) {
            throw e.toTaskException()
        }
    }

    override fun deleteTask(taskId: String): Boolean {
        return try {
            val listOfUpdatedTasks = getListWithDeletedTask(taskId)
            val csvContent = listOfUpdatedTasks.joinToString("\n") {
                taskCsvParser.parseTaskToString(it.toDto())
            }
            fileCsvWriter.updateCsvFile(csvContent)
            true
        } catch (e: Exception) {
            throw e.toTaskException()
        }
    }

    override fun createTask(task: Task): Boolean {
        return try {
            val taskRow = taskCsvParser.parseTaskToString(
                task.toDto().copy(
                    logs = listOf(
                        CreatedLogFormatter.format(
                            entityName = task.title,
                            entityType = EntityType.TASK,
                            username = task.createdBy,
                        )
                    )
                )
            )
            fileCsvWriter.writeToCsvFile(taskRow)
            true
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    override fun getAllTasks(): List<Task> {
        return fileCsvReader.readCsvFile().map { taskCsvParser.parseOneRowToTask(it).toDomain() }
    }

    override fun getListWithDeletedTask(taskId: String): List<Task> {
        return getAllTasks().let { tasks ->
            tasks.indexOfFirst { it.id.toString() == taskId }.let { taskIndex ->
                (tasks.subList(0, taskIndex) + tasks.subList(taskIndex + 1, tasks.size))
            }
        }
    }

    override fun getListOfUpdatedList(task: Task): List<Task> {
        return getAllTasks().let { tasks ->
            tasks.indexOfFirst { task.id == it.id }.let { taskIndex ->
                (tasks.subList(0, taskIndex) + task
//                    .copy(
//                    logs = task.logs + createLogsForUpdatedFields(
//                        oldTask =,
//                        updatedTask = task
//                    ))
                        + tasks.subList((taskIndex + 1), tasks.size))
            }
        }
    }

    override fun getTasksByProjectId(projectId: String): List<Task> {
        return getAllTasks().filter { it.projectId == projectId }
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

    override fun getLogsByTaskId(taskId: String): List<String> {
        val task = getAllTasks().find { it.id.toString() == taskId } ?: throw TaskExceptions.TaskNotFoundException()
        val taskLogs = task.logs
        if (taskLogs.isEmpty())
            throw TaskExceptions.NoLogsFoundException()
        return taskLogs
    }

}