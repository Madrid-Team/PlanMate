package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
) {

    fun createProject(project: Project) =
        projectRepository.createProject(project)


}