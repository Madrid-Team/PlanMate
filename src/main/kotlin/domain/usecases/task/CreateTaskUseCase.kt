package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val taskValidatorUseCase: TaskValidatorUseCase

) {
    suspend fun createTask(task: Task) {
         taskValidatorUseCase.validateAll(task)

        taskRepository.createTask(task)
    }
}