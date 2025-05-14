package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions

class GetProjectByIdUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun getProjectById(id: String): Project {
        if (id.isBlank()) {
            throw ProjectExceptions.ProjectNotFoundException("Project ID cannot be empty")
        }
        return projectRepository.getProjectById(id)
    }
}