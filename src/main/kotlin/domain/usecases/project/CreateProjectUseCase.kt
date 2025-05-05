package domain.usecases.project

import data.dto.project.ProjectDto
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.usecases.logs.CreateLogUseCase
import domain.utlis.*

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
) {

    fun createProject(project: Project): Result<Unit> {


        val result = projectRepository.createProject(project)

        return if (result.isSuccess) {
            Result.success(Unit)
        } else {
            Result.failure(result.exceptionOrNull()!!)
        }

    }

}