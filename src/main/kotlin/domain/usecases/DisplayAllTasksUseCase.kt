package domain.usecases

import domain.repository.ProjectRepository
import domain.repository.TaskRepository

class DisplayAllTasksUseCase(private val projectRepository: ProjectRepository,
                             private val taskRepository: TaskRepository
)
{
}