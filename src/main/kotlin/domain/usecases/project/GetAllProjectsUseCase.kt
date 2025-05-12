package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository

class GetAllProjectsUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun execute(): List<Project> {
        return projectRepository.getAllProjects()
    }
}