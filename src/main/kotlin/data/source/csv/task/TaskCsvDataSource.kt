package data.source.csv.task

import data.dto.task.TaskDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.taskHeader

class TaskCsvDataSource(
    private val taskCsvParser: TaskCsvParser,
    private val fileCsvWriter: FileCsvWriter,
    private val fileCsvReader: FileCsvReader,
    private val taskManager: TaskManager
) : TaskExternalDataSource {

    init {
        getAllTasks()
    }

    private fun getAllTasks(): List<TaskDto> {

        if (taskManager.getTasks().isEmpty()) {

            val tasks = mutableListOf<TaskDto>()
            fileCsvReader.readCsvFile().forEach { row ->
                if (row.isNotEmpty()) {
                    tasks.add(taskCsvParser.parseOneRowToTask(row))
                }
            }
            taskManager.setTasks(tasks)
        }
        return taskManager.getTasks()
    }

    override suspend fun editTask(task: TaskDto) {
        val taskListAfterEditTask = taskManager.editTask(task)
        var tasksFileContentAfterDeletion = String.taskHeader
        taskListAfterEditTask.forEach {
            val projectAsString = taskCsvParser.parseTaskToString(it)
            tasksFileContentAfterDeletion += projectAsString
        }
        fileCsvWriter.updateCsvFile(tasksFileContentAfterDeletion)
    }

    override suspend fun deleteTask(taskId: String) {
        val taskListAfterDeleteTask = taskManager.deleteTask(taskId)
        var tasksFileContentAfterDeletion = String.taskHeader
        taskListAfterDeleteTask.forEach {
            val projectAsString = taskCsvParser.parseTaskToString(it)
            tasksFileContentAfterDeletion += projectAsString
        }
        fileCsvWriter.updateCsvFile(tasksFileContentAfterDeletion)
    }

    override suspend fun createTask(task: TaskDto) {
        val taskRow = taskCsvParser.parseTaskToString(task)
        fileCsvWriter.writeToCsvFile(taskRow)
        taskManager.addTask(task)
    }

    override suspend fun getTasksByProjectId(projectId: String): List<TaskDto> {
        return taskManager.getTasks().filter { task -> task.projectId == projectId }
    }

    override suspend fun getTaskLogsByID(taskId: String): List<String> {
        return taskManager.getTasks().filter { it.id == taskId }.flatMap { it.taskLogs }
    }
}