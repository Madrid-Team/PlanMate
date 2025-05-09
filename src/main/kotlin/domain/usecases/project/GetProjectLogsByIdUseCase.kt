package domain.usecases.project

import domain.repository.ProjectRepository

class GetProjectLogsByIdUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(id: String): List<String> =
        projectRepository.getProjectLogsById(id)
}