package org.madrid.data.source.task

import data.dto.task.TaskDto
import data.source.task.TaskDataSource

class TaskMongoDBDataSource: TaskDataSource {
    override fun editTask(tasks: List<TaskDto>) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(task: List<TaskDto>) {
        TODO("Not yet implemented")
    }

    override fun createTask(task: TaskDto) {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): List<TaskDto> {
        TODO("Not yet implemented")
    }

    override fun getTasksByProjectId(projectId: String): List<TaskDto> {
        TODO("Not yet implemented")
    }

    override fun getTaskLogsByID(taskId: String): List<String> {
        TODO("Not yet implemented")
    }
}