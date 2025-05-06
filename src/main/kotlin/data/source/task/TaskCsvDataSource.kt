package data.source.task

import data.dto.task.TaskDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.taskHeader

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

    override fun getAllTasks(): List<TaskDto> {
        val tasks = mutableListOf<TaskDto>()
        fileCsvReader.readCsvFile().forEach { row ->
            if (row.isNotEmpty()) {
                tasks.add(taskCsvParser.parseOneRowToTask(row))
            }
        }
        return tasks
    }

    override fun getTasksByProjectId(projectId: String): List<TaskDto> {
        val allTasks = getAllTasks()
        return allTasks.ifEmpty {
            emptyList()
        }
    }

    override fun getTaskLogsByID(taskId: String): List<String> {
        return getAllTasks()
            .find { it.id == taskId }
            ?.logs ?: emptyList()
    }
}