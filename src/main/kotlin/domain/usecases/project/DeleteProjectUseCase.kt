package domain.usecases.project

import domain.repository.ProjectRepository

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
) {

    fun deleteProject(projectId: String) {
        projectRepository.deleteProject(projectId)
    }


}