package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase
import domain.utils.TaskExceptions

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
) {
    suspend fun editTask(task: Task) {
        // Verify project exists
        getProjectByIdUseCase.getById(task.projectId)

        // Verify task exists
        getTaskByIdUseCase.getTaskById(task.projectId, task.id.toString())

        if (task.title.isEmpty()) {
            throw TaskExceptions.TaskTitleIsEmptyException()
        }
        if (task.description.isEmpty()) {
            throw TaskExceptions.TaskDescriptionIsEmptyException()
        }
        taskRepository.editTask(task)
    }
}