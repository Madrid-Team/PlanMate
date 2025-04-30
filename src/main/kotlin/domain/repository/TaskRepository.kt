package domain.repository

import data.dto.task.Task

interface TaskRepository {
    fun getTasksByProjectId(projectId: String): List<Task>
}