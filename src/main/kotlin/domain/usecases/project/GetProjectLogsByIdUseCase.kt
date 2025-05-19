package domain.usecases.project

import domain.repository.ProjectRepository
import domain.utils.NoLogsFoundException

class GetProjectLogsByIdUseCase(
    private val projectRepository: ProjectRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun getProjectLogsByProjectId(projectId: String): List<String> {
        getProjectByIdUseCase.getProjectById(projectId)
        val logs = projectRepository.getProjectLogsByProjectId(projectId)
        logs.ifEmpty {
            throw NoLogsFoundException("There is no logs for this project")
        }
        return logs
    }
}