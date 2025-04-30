package data.repository

import domain.models.task.Task
import domain.repository.TaskRepository

class TaskRepositoryImpl : TaskRepository {
        override fun editTask(task: Task) {
            TODO("Not yet implemented")
        }

        override fun deleteTask(taskId: String) {
            TODO("Not yet implemented")
        }

        override fun createTask(task: Task): Boolean {
            TODO("Not yet implemented")
        }

        override fun getAllTasks(): List<Task> {
            TODO("Not yet implemented")
        }

    override fun getTasksByProjectId(projectId: String): List<Task> {
        TODO("Not yet implemented")
    }
}

