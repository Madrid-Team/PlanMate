package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase
import domain.utils.ProjectExceptions
import domain.utils.TaskExceptions

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase

) {
    suspend fun createTask(task: Task) {
        if (task.projectId.isBlank()) {
            throw ProjectExceptions.ProjectNotFoundException()
        }
        getProjectByIdUseCase.getById(task.projectId)
        if (task.title.isEmpty()) {
            throw TaskExceptions.TaskTitleIsEmptyException()
        }
        if (task.description.isEmpty()) {
            throw TaskExceptions.TaskDescriptionIsEmptyException()
        }
        if (task.taskState.isEmpty()) {
            throw TaskExceptions.TaskStateIsEmptyException()
        }
        taskRepository.createTask(task)
    }
}