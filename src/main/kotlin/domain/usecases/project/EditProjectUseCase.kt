package domain.usecases.project

import domain.models.logs.AuditLog
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.project.Project
import domain.repository.ProjectRepository

class EditProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val projectValidator: ProjectValidator,
     private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun editProject(updatedProject: Project) {
        with(projectValidator) {
            validate(updatedProject)
        }
        val currentProject = getProjectByIdUseCase.getProjectById(updatedProject.id.toString())
        projectRepository.editProject(
            updatedProject.copy(
                projectLogs = currentProject.projectLogs + getLogsForAllChangesInUpdatedProject(
                    currentProject,
                    updatedProject
                )
            )
        )
    }

    private fun getLogsForAllChangesInUpdatedProject(currentProject: Project, updatedProject: Project): List<String> {
        val listOfLogs = mutableListOf<String>()

        if (updatedProject.name != currentProject.name) {
            listOfLogs.add(
                    AuditLog(
                        operationType = OperationType.UPDATE,
                        entityName = currentProject.name,
                        entityType = EntityType.PROJECT,
                        username = currentProject.createdBy,
                        fieldName = "name",
                        oldValue = currentProject.name,
                        newValue = updatedProject.name,
                        id = TODO(),
                        entityId = TODO(),
                        projectId = TODO(),
                        timestamp = TODO(),
                    ).toString()
            )
        }

        if (updatedProject.description != currentProject.description) {
            listOfLogs.add(
                AuditLog(
                    operationType = OperationType.UPDATE,
                    entityName = currentProject.name,
                    entityType = EntityType.PROJECT,
                    username = currentProject.createdBy,
                    fieldName = "description",
                    oldValue = currentProject.description,
                    newValue = updatedProject.description,
                    id = TODO(),
                    entityId = TODO(),
                    projectId = TODO(),
                    timestamp = TODO(),
                ).toString()
            )
        }

        if (updatedProject.projectState != currentProject.projectState) {
            listOfLogs.add(
                AuditLog(
                    operationType = OperationType.UPDATE,
                    entityName = currentProject.name,
                    entityType = EntityType.PROJECT,
                    username = currentProject.createdBy,
                    fieldName = "project state",
                    oldValue = currentProject.projectState,
                    newValue = updatedProject.projectState,
                    id = TODO(),
                    entityId = TODO(),
                    projectId = TODO(),
                    timestamp = TODO(),
                ).toString()
            )
        }
        return listOfLogs
    }
}