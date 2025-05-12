package domain.usecases.task

import domain.repository.ProjectRepository

class GetAllTasksWithinProjectUseCase(
    private val projectRepository: ProjectRepository,
) {
    suspend fun getAllTasks(projectId: String): List<String> {
        val project = projectRepository.getProjectById(projectId)
        val tasksStates = project.taskStates
        return tasksStates
    }

}