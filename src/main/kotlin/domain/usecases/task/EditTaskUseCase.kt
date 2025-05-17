package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository

class EditTaskUseCase(
    private val taskRepository: TaskRepository,
    private val taskValidatorUseCase: TaskValidatorUseCase
) {
    suspend fun editTask(task: Task) {
        taskValidatorUseCase.validateBasic(task)
        taskRepository.editTask(task)
    } }