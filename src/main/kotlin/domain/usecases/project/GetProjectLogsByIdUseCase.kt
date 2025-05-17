package domain.usecases.project

import domain.repository.AuditLogRepository
import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions

class GetProjectLogsByIdUseCase(
    private val auditLogRepository: AuditLogRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun getProjectLogsByProjectId(projectId: String): List<String> {
        getProjectByIdUseCase.getProjectById(projectId)
        val logs = auditLogRepository.getAuditLogForProjectById(projectId)
        logs.ifEmpty {
            throw ProjectExceptions.NoLogsFoundException("There is no logs for this project")
        }
        return logs.map { it.toString() }
    }
}