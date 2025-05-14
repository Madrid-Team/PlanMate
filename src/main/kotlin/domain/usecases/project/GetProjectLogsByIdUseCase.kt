package domain.usecases.project

import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions

class GetProjectLogsByIdUseCase(
    private val projectRepository: ProjectRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun execute(id: String): List<String> {
        getProjectByIdUseCase.getProjectById(id)
        val logs = projectRepository.getProjectLogsByProjectId(id)
        logs.ifEmpty {
            throw ProjectExceptions.NoLogsFoundException("There is no logs for this project")
        }
        return logs
    }
}