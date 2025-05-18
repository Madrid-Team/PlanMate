package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions
import domain.utils.ProjectNotFoundException

class GetProjectByIdUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun getProjectById(projectId: String): Project {
        if (projectId.isBlank()) {
            throw ProjectNotFoundException("Project ID cannot be empty")
        }
        return projectRepository.getProjectById(projectId)
    }
}