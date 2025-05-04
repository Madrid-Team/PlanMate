package domain.usecases.project

import domain.repository.ProjectRepository

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository
) {

    fun deleteProject(projectId: String): Result<Unit> {
        val result = projectRepository.deleteProject(projectId)
        return if (result.isSuccess) {
            Result.success(Unit)
        } else {
            Result.failure(result.exceptionOrNull()!!)
        }
    }


}