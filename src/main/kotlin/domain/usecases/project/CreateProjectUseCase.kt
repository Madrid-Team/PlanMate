package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val projectValidator: ProjectValidator
) {
    suspend fun createProject(project: Project) {
        projectValidator.validate(project)
        projectRepository.createProject(project)
    }
}