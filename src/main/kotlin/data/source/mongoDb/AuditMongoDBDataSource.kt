package data.source.mongoDb

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.or
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.AuditLogDto
import data.source.AuditExternalDataSource
import data.utils.ENTITY_ID
import kotlinx.coroutines.flow.toList

class AuditMongoDBDataSource(
    private val collection: MongoCollection<AuditLogDto>
) : AuditExternalDataSource {
    override suspend fun addAuditLog(event: AuditLogDto) {
        collection.insertOne(event)
    }

    override suspend fun getAuditLogForTaskById(taskId: String): List<AuditLogDto> {
        val query = eq(ENTITY_ID, taskId)
        return collection.find(query).toList()
    }

    override suspend fun getAuditLogForProjectById(projectId: String): List<AuditLogDto> {
        val query = or(
            eq(ENTITY_ID, projectId), // all logs for project
            eq("projectId", projectId) // get project's tasks logs
        )
        return collection.find(query).toList()
    }

}