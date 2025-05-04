package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utlis.ProjectExceptions

class GetProjectByIdUseCase(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(id: String): Result<Project> {
        val project = projectRepository.getProjectById(id)
        return if (project != null) {
            Result.success(project)
        } else {
            Result.failure(ProjectExceptions.ProjectNotFoundException())
        }
    }
}