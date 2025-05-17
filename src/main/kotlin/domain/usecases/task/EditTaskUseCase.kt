package domain.usecases.task

import data.source.csv.user.CurrentUserProvider
import domain.models.logs.AuditLog
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.task.Task
import domain.repository.TaskRepository
import domain.usecases.logs.AddAuditLogUseCase

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val taskValidator: TaskValidator,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val addAuditLogUseCase: AddAuditLogUseCase,
    private val currentUserProvider: CurrentUserProvider
) {
    suspend fun editTask(updatedTask: Task) {
        taskValidator.validateBasic(updatedTask)
        updateTaskLogs(updatedTask)
        taskRepository.editTask(updatedTask)
    }

    private suspend fun updateTaskLogs(updatedTask: Task) {
        val currentTask = getTaskByIdUseCase.getTaskById(updatedTask.id.toString())
        if (updatedTask.description != currentTask.description) {
            addAuditLogUseCase.addAuditLog(
                AuditLog(
                    operationType = OperationType.UPDATE,
                    entityName = updatedTask.title,
                    entityType = EntityType.TASK,
                    entityId = updatedTask.id.toString(),
                    projectId = updatedTask.projectId,
                    username = currentUserProvider.getCurrentUser().username,
                    fieldName = "description",
                    oldValue = currentTask.description,
                    newValue = updatedTask.description
                )
            )
        }
        if (updatedTask.title != currentTask.title) {
            addAuditLogUseCase.addAuditLog(
                AuditLog(
                    operationType = OperationType.UPDATE,
                    entityName = updatedTask.title,
                    entityType = EntityType.TASK,
                    entityId = updatedTask.id.toString(),
                    projectId = updatedTask.projectId,
                    username = currentUserProvider.getCurrentUser().username,
                    fieldName = "title",
                    oldValue = currentTask.title,
                    newValue = updatedTask.title
                )
            )
        }
    }
}