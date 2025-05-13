package domain.usecases.project

import domain.repository.ProjectRepository

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun deleteProject(projectId: String) {
        getProjectByIdUseCase.getById(projectId)
        projectRepository.deleteProject(projectId)
    }
}