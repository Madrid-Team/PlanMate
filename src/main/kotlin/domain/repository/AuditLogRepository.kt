package domain.repository

import domain.models.logs.AuditLog

interface AuditLogRepository {
    suspend fun addAuditLog(event: AuditLog)
    suspend fun getAuditLogForTaskById(taskId: String): List<AuditLog>
    suspend fun getAuditLogForProjectById(projectId: String): List<AuditLog>
}