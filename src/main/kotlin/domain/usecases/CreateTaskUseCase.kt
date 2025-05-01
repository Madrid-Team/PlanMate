package domain.usecases

import domain.models.task.Task
import domain.repository.TaskRepository

class CreateTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun createTask(task: Task): Boolean {
        try {
            taskRepository.createTask(task)
        } catch (e: Exception) {
            println("Can't create task")
        }
        return true
    }
}