package data.source.csv.task

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.task.TaskDto
import data.utils.TASK_ID
import data.utils.TASK_PROJECT_ID
import kotlinx.coroutines.flow.toList

class TaskMongoDBDataSource(
    private val collection: MongoCollection<TaskDto>
) : TaskExternalDataSource {
    override suspend fun editTask(task: TaskDto) {
        val filter = eq(TASK_ID, task.id)
        collection.replaceOne(filter, task)
    }

    override suspend fun deleteTask(taskId: String) {
        val filter = eq(TASK_ID, taskId)
        collection.deleteOne(filter)
    }

    override suspend fun createTask(task: TaskDto) {
        collection.insertOne(task)
    }

    override suspend fun getTasksByProjectId(projectId: String): List<TaskDto> {
        val filter = eq(TASK_PROJECT_ID, projectId)
        return collection.find(filter).toList()
    }

    override suspend fun getTaskLogsByID(taskId: String): List<String> {
        val projectFilter = eq(TASK_ID, taskId)
        return collection.find(projectFilter).toList().flatMap { it.logs }.toList()
    }
}