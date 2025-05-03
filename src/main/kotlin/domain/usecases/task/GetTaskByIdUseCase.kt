package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository

class GetTaskByIdUseCase(private val taskRepository: TaskRepository) {
    operator fun invoke(takId: String): Task? {
        return taskRepository.getAllTasks().getOrNull()?.find { it.id.toString() == takId }
    }
}