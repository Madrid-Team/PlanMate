package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.utlis.TaskNotFoundException

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun deleteTask(taskId: String): Boolean {
        getTaskById(taskId) ?: throw TaskNotFoundException()
        taskRepository.deleteTask(taskId)
        return true
    }

    private fun getTaskById(taskId: String): Task? {
        return taskRepository.getAllTasks().find { it.id == taskId }
    }
}