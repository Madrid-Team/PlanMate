package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val taskValidator: TaskValidator
) {
    suspend fun editTask(task: Task) {
        taskValidator.validateBasic(task)
        taskRepository.editTask(task)
    } }