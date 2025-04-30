package data.source

import data.utils.FileCsvWriter
import domain.models.task.Task

class TaskCsvDataSource(
    private val taskCsvParser: TaskCsvParser,
    private val fileCsvWriter: FileCsvWriter
) : TaskDataSource {
    override fun editTask(task: Task): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun createTask(task: Task): Result<Unit> {
        val taskRow = taskCsvParser.parseTaskToString(task)
        fileCsvWriter.writeProjectToCsvFile(taskRow)
        return Result.success(Unit)
    }

    override fun getAllTasks(): Result<Task> {
        TODO("Not yet implemented")
    }

}