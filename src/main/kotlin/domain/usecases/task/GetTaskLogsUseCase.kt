package domain.usecases.task

import domain.repository.AuditLogRepository
import domain.repository.ProjectRepository
import domain.repository.TaskRepository
import domain.usecases.logs.AddAuditLogUseCase
import domain.utils.TaskExceptions

class GetTaskLogsUseCase(
    private val addAuditLoRepository: AuditLogRepository
) {
    suspend  fun getTaskLogsByTaskId(taskId: String): List<String> {
        return addAuditLoRepository.getAuditLogForTaskById(taskId).map { it.toString() }.ifEmpty { throw TaskExceptions.NoLogsFoundException() }
    }

}