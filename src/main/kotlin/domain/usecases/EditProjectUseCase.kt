package domain.usecases

import domain.models.project.Project
import domain.repository.ProjectRepository

class EditProjectUseCase(private val projectRepository: ProjectRepository) {

    fun editProject(project: Project):Boolean {
        return false
    }
}