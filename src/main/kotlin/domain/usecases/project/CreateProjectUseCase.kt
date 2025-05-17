package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val projectValidatorUseCase: ProjectValidatorUseCase
) {
    suspend fun createProject(project: Project) {
        projectValidatorUseCase.validate(project)
        projectRepository.createProject(project)
    }
}