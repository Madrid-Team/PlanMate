package data.source.task

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.project.ProjectDto
import data.dto.task.TaskDto
import kotlinx.coroutines.flow.toList
import org.bson.Document

class TaskMongoDBDataSource(
    private val collection: MongoCollection<TaskDto>
) : TaskExternalDataSource {
    override suspend fun editTask(task: TaskDto) {
        val filter = eq("_id", task.projectId)
        collection.replaceOne(filter, task)
    }

    override suspend fun deleteTask(projectId: String, taskId: String) {
        val filter = eq("_id", taskId)
        collection.deleteOne(filter)
    }

    override suspend fun createTask(task: TaskDto) {
        collection.insertOne(task)
    }

    override suspend fun getTasksByProjectId(projectId: String): List<TaskDto> {
        val filter = eq("projectId", projectId)
        return collection.find(filter).toList()
    }

    override suspend fun getTaskLogsByID(projectId: String, taskId: String): List<String> {
        val projectFilter = eq("_id", taskId)
        return collection.find(projectFilter).toList().flatMap { it.logs }.toList()
    }
}