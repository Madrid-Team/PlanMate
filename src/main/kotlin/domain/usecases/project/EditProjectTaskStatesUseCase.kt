package domain.usecases.project

import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.repository.ProjectRepository
import domain.usecases.logs.CreateLogUseCase
import domain.utils.ProjectExceptions
import java.util.UUID

class EditProjectTaskStatesUseCase(
    private val projectRepository: ProjectRepository,
    private val createLogUseCase: CreateLogUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun execute(projectId: UUID, newTaskStates: List<String>) {
        val currentProject = getProjectByIdUseCase.getById(projectId.toString())

        // Check if task states are unchanged
        if (newTaskStates == currentProject.taskStates) {
            throw ProjectExceptions.NoChangesException("Project task states are unchanged")
        }

        // Create log for the task states change
        val log = createLogUseCase.invoke(
            operationType = OperationType.UPDATE,
            entityName = currentProject.name,
            entityType = EntityType.PROJECT,
            username = currentProject.createdBy,
            fieldName = "task states",
            oldValue = currentProject.taskStates.joinToString(", "),
            newValue = newTaskStates.joinToString(", ")
        )

        // Update the project with new task states and log
        val updatedProject = currentProject.copy(
            taskStates = newTaskStates,
            projectLogs = currentProject.projectLogs + log
        )

        projectRepository.editProject(updatedProject)
    }
}