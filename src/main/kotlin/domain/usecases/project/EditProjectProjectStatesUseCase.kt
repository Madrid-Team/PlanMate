package domain.usecases.project

import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.repository.ProjectRepository
import domain.usecases.logs.CreateLogUseCase
import domain.utils.ProjectExceptions
import java.util.UUID

class EditProjectProjectStatesUseCase(
    private val projectRepository: ProjectRepository,
    private val createLogUseCase: CreateLogUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun execute(projectId: UUID, newProjectStates: List<String>) {
        val currentProject = getProjectByIdUseCase.getById(projectId.toString())

        // Check if project states are unchanged
        if (newProjectStates == currentProject.projectStates) {
            throw ProjectExceptions.NoChangesException("Project states are unchanged")
        }

        // Create log for the project states change
        val log = createLogUseCase.invoke(
            operationType = OperationType.UPDATE,
            entityName = currentProject.name,
            entityType = EntityType.PROJECT,
            username = currentProject.createdBy,
            fieldName = "project states",
            oldValue = currentProject.projectStates.joinToString(", "),
            newValue = newProjectStates.joinToString(", ")
        )

        // Update the project with new project states and log
        val updatedProject = currentProject.copy(
            projectStates = newProjectStates,
            projectLogs = currentProject.projectLogs + log
        )

        projectRepository.editProject(updatedProject)
    }
}