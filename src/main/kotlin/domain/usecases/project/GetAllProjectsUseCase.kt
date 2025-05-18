package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions
import domain.utils.ProjectNotFoundException

class GetAllProjectsUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun getProjects(): List<Project> {
        return projectRepository.getAllProjects().ifEmpty { throw ProjectNotFoundException() }
    }
}