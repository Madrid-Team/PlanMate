package domain.usecases.task

import domain.repository.ProjectRepository

class DisplayAllTasksUseCase(
    private val projectRepository: ProjectRepository,
) {
    suspend fun display(projectId: String): List<String> {
        val project = projectRepository.getProjectById(projectId)
        val tasksStates = project.taskStates
        return tasksStates
    }
}