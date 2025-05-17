package domain.usecases.project

import data.source.csv.user.CurrentUserProvider
import domain.models.logs.AuditLog
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.usecases.logs.AddAuditLogUseCase

class EditProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val projectValidator: ProjectValidator,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val addAuditLogUseCase: AddAuditLogUseCase,
    private val currentUserProvider: CurrentUserProvider
) {
    suspend fun editProject(updatedProject: Project) {
        with(projectValidator) {
            validate(updatedProject)
        }
        val currentProject = getProjectByIdUseCase.getProjectById(updatedProject.id.toString())
        projectRepository.editProject(updatedProject)
        updateProjectLogs(currentProject = currentProject, updatedProject = updatedProject)
    }

    private suspend fun updateProjectLogs(currentProject: Project, updatedProject: Project) {
        if (updatedProject.name != currentProject.name) {
            addAuditLogUseCase.addAuditLog(
                AuditLog(
                    operationType = OperationType.UPDATE,
                    entityName = updatedProject.name,
                    entityType = EntityType.PROJECT,
                    entityId = updatedProject.id.toString(),
                    username = currentUserProvider.getCurrentUser().username,
                    fieldName = "name",
                    oldValue = currentProject.name,
                    newValue = updatedProject.name,
                )
            )
        }

        if (updatedProject.description != currentProject.description) {
            addAuditLogUseCase.addAuditLog(
                AuditLog(
                    operationType = OperationType.UPDATE,
                    entityName = updatedProject.name,
                    entityType = EntityType.PROJECT,
                    entityId = updatedProject.id.toString(),
                    username = currentUserProvider.getCurrentUser().username,
                    fieldName = "description",
                    oldValue = currentProject.description,
                    newValue = updatedProject.description,
                )
            )
        }

        if (updatedProject.projectState != currentProject.projectState) {
            addAuditLogUseCase.addAuditLog(
                AuditLog(
                    operationType = OperationType.UPDATE,
                    entityName = updatedProject.name,
                    entityType = EntityType.PROJECT,
                    entityId = updatedProject.id.toString(),
                    username = currentUserProvider.getCurrentUser().username,
                    fieldName = "project State",
                    oldValue = currentProject.projectState,
                    newValue = updatedProject.projectState,
                )
            )
        }
    }
}