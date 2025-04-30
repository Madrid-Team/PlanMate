package data.repository

import domain.models.task.Task
import domain.repository.TaskRepository

class TaskRepositoryImpl : TaskRepository {
    override fun editTask(taskId: String, updatedTask: Task) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun createTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }
}