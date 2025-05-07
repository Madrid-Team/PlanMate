package org.madrid.data.source.task

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.project.ProjectDto
import data.dto.task.TaskDto
import kotlinx.coroutines.flow.toList
import org.bson.Document
import org.madrid.data.utils.PROJECT_COLLECTION

class TaskMongoDBDataSource(
    database: MongoDatabase

) : RemoteTaskDataSource {
    private val collection = database.getCollection<ProjectDto>(PROJECT_COLLECTION)
    override suspend fun editTask(task: TaskDto) {
        val filter = Filters.and(
            eq("_id", task.projectId),
            eq("tasks.id", task.id)
        )
        val update = Updates.set("tasks.$", task)
        collection.updateOne(filter, update)
    }

    override suspend fun deleteTask(projectId: String, taskId: String) {
        val filter = eq("_id", projectId)
        val update = Updates.pull("tasks", Document("id", taskId))
        collection.updateOne(filter, update)
    }

    override suspend fun createTask(task: TaskDto) {

        val filter = eq("_id", task.projectId)
        val update = Updates.push("tasks", task)

        collection.updateOne(filter, update)
    }

    override suspend fun getTasksByProjectId(projectId: String): List<TaskDto> {
        val filter = eq("_id", projectId)
        return collection.find(filter).toList().flatMap { it.tasks }
    }

    override suspend fun getTaskLogsByID(projectId: String, taskId: String): List<String> {
        val projectFilter = eq("_id", projectId)
        return collection.find(projectFilter).toList().flatMap { it.tasks }.flatMap { it.logs }.toList()

    }
}