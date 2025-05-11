package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.validation.ValidateProjectName

class EditProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val validateProjectName: ValidateProjectName
) {
    suspend operator fun invoke(project: Project) {
        validateProjectName(project)
        projectRepository.editProject(project)
    }
}