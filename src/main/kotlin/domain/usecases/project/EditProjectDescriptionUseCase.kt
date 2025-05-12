package domain.usecases.project

import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.repository.ProjectRepository
import domain.usecases.logs.CreateLogUseCase
import domain.utils.ProjectExceptions
import java.util.UUID

class EditProjectDescriptionUseCase(
    private val projectRepository: ProjectRepository,
    private val createLogUseCase: CreateLogUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun execute(projectId: UUID, newDescription: String) {
        val currentProject = getProjectByIdUseCase.getById(projectId.toString())

        // Check if description is unchanged
        if (newDescription == currentProject.description) {
            throw ProjectExceptions.NoChangesException("Project description is unchanged")
        }

        // Create log for the description change
        val log = createLogUseCase.invoke(
            operationType = OperationType.UPDATE,
            entityName = currentProject.name,
            entityType = EntityType.PROJECT,
            username = currentProject.createdBy,
            fieldName = "description",
            oldValue = currentProject.description,
            newValue = newDescription
        )

        // Update the project with new description and log
        val updatedProject = currentProject.copy(
            description = newDescription,
            projectLogs = currentProject.projectLogs + log
        )

        projectRepository.editProject(updatedProject)
    }
}