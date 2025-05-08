package data.source.task

import data.dto.task.TaskDto
import domain.models.task.Task

class TaskManager {

    private val tasks = mutableListOf<TaskDto>()

    fun getTasks(): List<TaskDto> {
        return tasks.toList()
    }

    fun setTasks(tasks: List<TaskDto>) {
        this.tasks.addAll(tasks)
    }

    fun addTask(task: TaskDto) {
        tasks.add(task)
    }

    fun deleteTask(taskId: String): List<TaskDto> {
        tasks.removeIf { it.id.toString() == taskId }
        return tasks
    }

    fun editTask(task: TaskDto): List<TaskDto> {
        val updatedIndex = tasks.indexOfFirst { it.id == task.id }
        tasks[updatedIndex] = task
        return tasks
    }

    fun getTaskLogsById(taskId: String): List<String> {
        return tasks.find { it.id.toString() == taskId }
            ?.logs ?: emptyList()
    }
}
