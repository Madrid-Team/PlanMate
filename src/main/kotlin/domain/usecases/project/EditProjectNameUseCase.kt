package domain.usecases.project

import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.repository.ProjectRepository
import domain.usecases.logs.CreateLogUseCase
import domain.utils.ProjectExceptions
import domain.validation.ValidateProjectName
import java.util.*

class EditProjectNameUseCase(
    private val projectRepository: ProjectRepository,
    private val validateProjectName: ValidateProjectName,
    private val createLogUseCase: CreateLogUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun execute(projectId: UUID, newName: String) {
        val currentProject = getProjectByIdUseCase.getById(projectId.toString())

        // Check if name is unchanged
        if (newName == currentProject.name) {
            throw ProjectExceptions.NoChangesException("Project name is unchanged")
        }

        // Validate the new name
        validateProjectName(currentProject.copy(name = newName))

        // Create log for the name change
        val log = createLogUseCase.invoke(
            operationType = OperationType.UPDATE,
            entityName = currentProject.name,
            entityType = EntityType.PROJECT,
            username = currentProject.createdBy,
            fieldName = "name",
            oldValue = currentProject.name,
            newValue = newName
        )

        // Update the project with new name and log
        val updatedProject = currentProject.copy(
            name = newName,
            projectLogs = currentProject.projectLogs + log
        )

        projectRepository.editProject(updatedProject)
    }
}