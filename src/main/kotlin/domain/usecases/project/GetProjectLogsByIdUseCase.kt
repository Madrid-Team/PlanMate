package domain.usecases.project

import domain.repository.ProjectRepository

class GetProjectLogsByIdUseCase(
    private val projectRepository : ProjectRepository
) {
    fun getProjectLogsById(id: String) : Result<List<String>> {
        return projectRepository.getProjectLogsById(id)
    }
}