package domain.usecases.project

import domain.repository.AuditLogRepository
import domain.utils.ProjectExceptions

class GetProjectLogsByIdUseCase(
    private val auditLogRepository: AuditLogRepository
) {
    suspend fun getProjectLogsByProjectId(projectId: String): List<String> {
        val logs = auditLogRepository.getAuditLogForProjectById(projectId)
        logs.ifEmpty {
            throw ProjectExceptions.NoLogsFoundException("There is no logs for this project")
        }
        return logs.map { it.toString() }
    }
}