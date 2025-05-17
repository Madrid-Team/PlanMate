package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val taskValidator: TaskValidator

) {
    suspend fun createTask(task: Task) {
         taskValidator.validateAll(task)

        taskRepository.createTask(task)
    }
}