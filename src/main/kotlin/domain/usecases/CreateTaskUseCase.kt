package domain.usecases

import domain.models.task.Task
import domain.repository.TaskRepository

class CreateTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun createTask(task: Task): Boolean {
        if (task.title.isBlank()) return false
        return taskRepository.createTask(task)
    }
}