package domain.usecases.project

import domain.repository.ProjectRepository

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
) {
    suspend operator fun invoke(projectId: String) {
        projectRepository.deleteProject(projectId)
    }
}