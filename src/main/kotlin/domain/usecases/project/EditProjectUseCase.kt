package domain.usecases.project

import data.dto.project.ProjectDto
import domain.models.project.Project
import domain.repository.ProjectRepository

class EditProjectUseCase(private val projectRepository: ProjectRepository) {

    fun editProject(project: ProjectDto): Result<Unit>{
        return projectRepository.editProject(project)
    }
}