package data.source.task

import data.utils.FileCsvWriter
import domain.models.task.Task

class TaskCsvDataSource(
    private val taskCsvParser: TaskCsvParser,
    private val fileCsvWriter: FileCsvWriter
) : TaskDataSource {
    override fun editTask(task: Task): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun createTask(task: Task): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

}