package data.source

import domain.models.task.Task

class TaskCsvDataSource : TaskDataSource {
    override fun editTask(task: Task): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun createTask(task: Task): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): Result<Task> {
        TODO("Not yet implemented")
    }

}