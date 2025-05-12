package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val taskValidator: TaskValidator
) {
    suspend fun editTask(task: Task) {
        // Verify project exists
        getProjectByIdUseCase.getById(task.projectId)

        // Verify task exists
        getTaskByIdUseCase.getTaskById(task.projectId, task.id.toString())

        taskValidator.validateBasic(task)
        taskRepository.editTask(task)
    }
}