package domain.usecases.project

import domain.repository.ProjectRepository

class GetProjectLogsByIdUseCase(
    private val projectRepository: ProjectRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun execute(id: String): List<String> {
        getProjectByIdUseCase.getById(id)

        return projectRepository.getProjectLogsById(id)
    }
}