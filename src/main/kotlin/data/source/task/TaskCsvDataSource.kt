package data.source.task

import data.dto.task.TaskDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.taskHeader
import data.utils.toTaskException

class TaskCsvDataSource(
    private val taskCsvParser: TaskCsvParser,
    private val fileCsvWriter: FileCsvWriter,
    private val fileCsvReader: FileCsvReader,
    private val taskManager: TaskManager
) : ExternalTaskDataSource {

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
        try {
            val taskListAfterEditTask = taskManager.editTask(task)
            var tasksFileContentAfterDeletion = String.taskHeader
            taskListAfterEditTask.forEach {
                val projectAsString = taskCsvParser.parseTaskToString(it)
                tasksFileContentAfterDeletion += projectAsString
            }
            fileCsvWriter.updateCsvFile(tasksFileContentAfterDeletion)

        } catch (exception: Exception) {
            throw exception.toTaskException()
        }

    }

    override suspend fun deleteTask(projectId: String, taskId: String) {
        try {
            val taskListAfterDeleteTask = taskManager.deleteTask(taskId)
            var tasksFileContentAfterDeletion = String.taskHeader
            taskListAfterDeleteTask.forEach {
                val projectAsString = taskCsvParser.parseTaskToString(it)
                tasksFileContentAfterDeletion += projectAsString
            }
            fileCsvWriter.updateCsvFile(tasksFileContentAfterDeletion)

        } catch (exception: Exception) {
            throw exception.toTaskException()
        }
    }

    override suspend fun createTask(task: TaskDto) {

        try {

            val taskRow = taskCsvParser.parseTaskToString(task)
            fileCsvWriter.writeToCsvFile(taskRow)
            taskManager.addTask(task)

        } catch (exception: Exception) {
            throw exception.toTaskException()
        }
    }

    override suspend fun getTasksByProjectId(projectId: String): List<TaskDto> {
        return taskManager.getTasks().filter { task -> task.projectId == projectId }
    }

    override suspend fun getTaskLogsByID(projectId: String, taskId: String): List<String> {
        return taskManager.getTasks().filter { it.id == taskId }.flatMap { it.logs }
    }
}