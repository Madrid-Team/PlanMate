package data.source.task

import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.models.task.Task
import domain.utlis.TaskNotFoundException
import java.io.IOException

class TaskCsvDataSource(
    private val taskCsvParser: TaskCsvParser,
    private val fileCsvWriter: FileCsvWriter,
    private val fileCsvReader: FileCsvReader
) : TaskDataSource {
    override fun editTask(task: Task): Boolean {
        return try {
            val updatedTasks = getListOfUpdatedList(task)
            val csvContent = updatedTasks.joinToString("\n") { taskCsvParser.parseTaskToString(it) }
            fileCsvWriter.updateCsvFile(csvContent)
            true
        } catch (e: Exception) {
            throw TaskNotFoundException(e.message)
        }
    }

    override fun deleteTask(taskId: String): Boolean {
        return try {
            val listOfUpdatedTasks = getListWithDeletedTask(taskId)
            val csvContent = listOfUpdatedTasks.joinToString("\n") {
                taskCsvParser.parseTaskToString(it)
            }
            fileCsvWriter.updateCsvFile(csvContent)
            true
        } catch (e: Exception) {
            throw TaskNotFoundException(e.message)
        }
    }

    override fun createTask(task: Task): Boolean {
        return try {
            val taskRow = taskCsvParser.parseTaskToString(task)
            fileCsvWriter.writeToCsvFile(taskRow)
            true
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    override fun getAllTasks(): List<Task> {
        return fileCsvReader.readCsvFile().map { taskCsvParser.parseOneRowToTask(it) }
    }

    override fun getListWithDeletedTask(taskId: String): List<Task> {
        return getAllTasks().let { tasks ->
            tasks.indexOfFirst { it.id == taskId }.let { taskIndex ->
                (tasks.subList(0, taskIndex) + tasks.subList(taskIndex + 1, tasks.size))
            }
        }
    }

    override fun getListOfUpdatedList(task: Task): List<Task> {
        return getAllTasks().let { tasks ->
            tasks.indexOfFirst { task.id == it.id }.let { taskIndex ->
                (tasks.subList(0, taskIndex) + task + tasks.subList((taskIndex + 1), tasks.size))
            }
        }
    }

}