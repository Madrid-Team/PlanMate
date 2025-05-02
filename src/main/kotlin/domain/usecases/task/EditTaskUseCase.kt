package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import domain.utlis.TaskNotFoundException

class EditTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun editTask(oldTaskId: String, updatedTask: Task): Boolean {
        val oldTask = getTaskById(oldTaskId) ?: throw TaskNotFoundException()
        val newTask = oldTask.copy(
            projectId = updatedTask.projectId.ifBlank { oldTask.projectId },
            title = updatedTask.title.ifBlank { oldTask.title },
            description = updatedTask.title.ifBlank { oldTask.description },
            state = updatedTask.state.ifBlank { oldTask.state },
            createdBy = updatedTask.state.ifBlank { oldTask.createdBy },
            logs = updatedTask.logs.ifEmpty { oldTask.logs }
        )
        taskRepository.editTask(newTask)
        return true
    }


    private fun getTaskById(taskId: String): Task? {
        return taskRepository.getAllTasks().find { it.id == taskId }
    }
}