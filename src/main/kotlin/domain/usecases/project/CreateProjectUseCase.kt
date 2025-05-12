package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utils.PlanMateExceptions
import domain.validation.ValidateProjectName

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val projectValidator: ProjectValidator
) {
    suspend fun execute(project: Project) {
        projectValidator.validate(project)
        projectRepository.createProject(project)
    }
}