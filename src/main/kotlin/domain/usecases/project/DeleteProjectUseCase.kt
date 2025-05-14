package domain.usecases.project

import domain.repository.ProjectRepository

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun deleteProjectByProjectId(projectId: String) {
        getProjectByIdUseCase.getProjectById(projectId)
        projectRepository.deleteProjectByProjectId(projectId)
    }
}