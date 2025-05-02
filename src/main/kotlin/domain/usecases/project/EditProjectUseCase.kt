package domain.usecases.project

import data.dto.project.ProjectDto
import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utlis.*

class EditProjectUseCase(private val projectRepository: ProjectRepository) {

    fun editProject(project: Project): Result<Unit> {

        try {
            validateName(project)
            val result = projectRepository.editProject(project)
            return if (result.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } catch (e: PlanMateExceptions) {
            return Result.failure(e)
        }
    }

    private fun validateName(project: Project) {
        if (!project.name.matches(Regex("^[A-Za-z ]+$"))) {
            throw ProjectNameInvalidException()
        }
    }
}