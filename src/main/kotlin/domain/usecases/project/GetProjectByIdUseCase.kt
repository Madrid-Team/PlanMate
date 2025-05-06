package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository

class GetProjectByIdUseCase(
    private val projectRepository: ProjectRepository
) {
    fun invoke(id: String): Project = projectRepository.getProjectById(id)
}