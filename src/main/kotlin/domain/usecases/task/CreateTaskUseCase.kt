package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val taskValidator: TaskValidator

) {
    suspend fun createTask(task: Task) {
        getProjectByIdUseCase.getById(task.projectId)
        taskValidator.validateAll(task)

        taskRepository.createTask(task)
    }
}