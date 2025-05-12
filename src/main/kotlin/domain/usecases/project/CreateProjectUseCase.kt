package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utils.PlanMateExceptions
import domain.validation.ValidateProjectName

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val validateProjectName: ValidateProjectName
) {
    suspend fun execute(project: Project) {
        // Validate project data
        validateProjectName(project)

        // Additional validations
        if (project.description.isBlank()) {
            throw PlanMateExceptions("Project description cannot be empty")
        }

        if (project.projectStates.isEmpty()) {
            throw PlanMateExceptions("Project states cannot be empty")
        }

        if (project.taskStates.isEmpty()) {
            throw PlanMateExceptions("Task states cannot be empty")
        }

        projectRepository.createProject(project)
    }
}