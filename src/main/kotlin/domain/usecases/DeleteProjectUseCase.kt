package domain.usecases

import domain.models.project.Project
import domain.repository.ProjectRepository

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository
) {

    fun deleteProject(projectId: String): Result<Unit> {
        val result = projectRepository.deleteProject(projectId)
        return result
    }


}