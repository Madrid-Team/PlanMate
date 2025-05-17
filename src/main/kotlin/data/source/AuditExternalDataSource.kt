package data.source

import data.dto.AuditLogDto

interface AuditExternalDataSource {
    suspend fun addAuditLog(event: AuditLogDto)
    suspend fun getAuditLogForTaskById(taskId: String): List<AuditLogDto>
    suspend fun getAuditLogForProjectById(projectId: String): List<AuditLogDto>
}