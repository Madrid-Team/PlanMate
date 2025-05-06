package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utlis.ProjectExceptions

class EditProjectUseCase(private val projectRepository: ProjectRepository) {

    fun editProject(project: Project) {
        validateName(project)
        projectRepository.editProject(project)
    }

    private fun validateName(project: Project) {
        if (!project.name.matches(Regex("^[A-Za-z ]+$"))) {
            throw ProjectExceptions.ProjectNameInvalidException()
        }
    }
}