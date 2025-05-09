package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
) {
    suspend operator fun invoke(project: Project) =
        projectRepository.createProject(project)
}