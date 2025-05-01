package domain.usecases.project

import data.dto.project.ProjectDto
import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utlis.*

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {

    fun createProject(project: Project): Result<Unit> {
        try {

            validateProject(project)

            val result = projectRepository.createProject(project)

            return if (result.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }

        } catch (e: PlanMateExceptions) {
            return Result.failure(e)
        }
    }

    private fun validateProject(project: Project) {
        if (project.name.isEmpty()) {
            throw ProjectNameInvalidException()
        }
        if (project.description.isEmpty()) {
            throw ProjectDescriptionInvalidException()
        }
        if (project.projectStates.isEmpty()) {
            throw ProjectStatesInvalidException()
        }
        if (project.taskStates.isEmpty()) {
            throw ProjectTaskStatesInvalidException()
        }
    }

}