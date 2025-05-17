package data.source.csv.task

import data.dto.task.TaskDto

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
        tasks.removeIf { it.id == taskId }
        return tasks
    }

    fun editTask(task: TaskDto): List<TaskDto> {
        val updatedIndex = tasks.indexOfFirst { it.id == task.id }
        tasks[updatedIndex] = task
        return tasks
    }

}
