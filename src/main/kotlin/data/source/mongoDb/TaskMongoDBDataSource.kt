package data.source.mongoDb

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.task.TaskDto
import data.source.csv.task.TaskExternalDataSource
import data.utils.TASK_ID
import data.utils.TASK_PROJECT_ID
import kotlinx.coroutines.flow.toList

class TaskMongoDBDataSource(
    private val collection: MongoCollection<TaskDto>
) : TaskExternalDataSource {
    override suspend fun editTask(task: TaskDto) {
        val filter = Filters.eq(TASK_ID, task.id)
        collection.replaceOne(filter, task)
    }

    override suspend fun deleteTask(taskId: String) {
        val filter = Filters.eq(TASK_ID, taskId)
        collection.deleteOne(filter)
    }

    override suspend fun createTask(task: TaskDto) {
        collection.insertOne(task)
    }

    override suspend fun getTasksByProjectId(projectId: String): List<TaskDto> {
        val filter = Filters.eq(TASK_PROJECT_ID, projectId)
        return collection.find(filter).toList()
    }

    override suspend fun getTaskLogsByID(taskId: String): List<String> {
        val projectFilter = Filters.eq(TASK_ID, taskId)
        return collection.find(projectFilter).toList().flatMap { it.taskLogs }.toList()
    }
}