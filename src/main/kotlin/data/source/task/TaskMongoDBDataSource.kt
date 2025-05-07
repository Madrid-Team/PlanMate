package org.madrid.data.source.task

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.project.ProjectDto
import data.dto.task.TaskDto
import kotlinx.coroutines.flow.toList
import org.madrid.data.utils.PROJECT_COLLECTION

class TaskMongoDBDataSource(
    private val database: MongoDatabase

) : RemoteTaskDataSource {
    private val collection = database.getCollection<ProjectDto>(PROJECT_COLLECTION)
    override suspend fun editTask(tasks: TaskDto) {
        return
    }

    override suspend fun deleteTask(projectId: String,taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(task: TaskDto) {
        collection.updateOne(
            filter = Filters.eq("id", task.projectId),
            update = Updates.push("tasks", task)
        )
    }

    override suspend fun getTasksByProjectId(projectId: String): List<TaskDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskLogsByID(projectId: String, taskId: String): List<String> {
        val projectFilter = Filters.eq("id", projectId)
        return collection.find(projectFilter).toList().flatMap { it.tasks }.flatMap { it.logs }.toList()

    }
}