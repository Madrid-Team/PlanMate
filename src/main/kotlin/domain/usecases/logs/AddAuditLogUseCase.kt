package domain.usecases.logs

import domain.models.logs.AuditLog
import domain.repository.AuditLogRepository

class AddAuditLogUseCase(
    private val auditLogRepository: AuditLogRepository
) {
    suspend fun addAuditLog(auditLog: AuditLog) {
        auditLogRepository.addAuditLog(auditLog)
    }
}