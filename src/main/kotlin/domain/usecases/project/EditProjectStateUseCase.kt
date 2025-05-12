package domain.usecases.project

import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.repository.ProjectRepository
import domain.usecases.logs.CreateLogUseCase
import domain.utils.ProjectExceptions
import java.util.UUID

class EditProjectStateUseCase(
    private val projectRepository: ProjectRepository,
    private val createLogUseCase: CreateLogUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun execute(projectId: UUID, newState: String) {
        val currentProject = getProjectByIdUseCase.getById(projectId.toString())

        // Check if state is unchanged
        if (newState == currentProject.projectState) {
            throw ProjectExceptions.NoChangesException("Project state is unchanged")
        }

        // Create log for the state change
        val log = createLogUseCase.invoke(
            operationType = OperationType.UPDATE,
            entityName = currentProject.name,
            entityType = EntityType.PROJECT,
            username = currentProject.createdBy,
            fieldName = "project state",
            oldValue = currentProject.projectState,
            newValue = newState
        )

        // Update the project with new state and log
        val updatedProject = currentProject.copy(
            projectState = newState,
            projectLogs = currentProject.projectLogs + log
        )

        projectRepository.editProject(updatedProject)
    }
}