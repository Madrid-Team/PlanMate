package data.repository

import data.dto.task.Task
import domain.repository.TaskRepository

class TaskRepositoryImpl():TaskRepository {
    override fun getTasksByProjectId(projectId: String): List<Task> {
        TODO("Not yet implemented")
    }
}