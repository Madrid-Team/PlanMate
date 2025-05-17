package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.AuditExternalDataSource
import data.utils.toAuditException
import domain.models.logs.AuditLog
import domain.repository.AuditLogRepository

class AuditLogRepositoryImpl(
    private val auditExternalDataSource: AuditExternalDataSource
):AuditLogRepository {
    override suspend fun addAuditLog(event: AuditLog) = executeAuditOperation {
        auditExternalDataSource.addAuditLog(event.toDto())
    }

    override suspend fun getAuditLogForTaskById(taskId: String): List<AuditLog> = executeAuditOperation {
        auditExternalDataSource.getAuditLogForTaskById(taskId).map { it.toDomain() }
    }

    override suspend fun getAuditLogForProjectById(projectId: String): List<AuditLog> = executeAuditOperation {
        auditExternalDataSource.getAuditLogForProjectById(projectId).map { it.toDomain() }
    }
    private suspend fun <T> executeAuditOperation(operation: suspend () -> T): T {
        return try {
            operation()
        } catch (e: Exception) {
            throw e.toAuditException()
        }
    }
}