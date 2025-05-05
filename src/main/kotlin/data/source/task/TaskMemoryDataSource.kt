package data.source.task

import domain.models.task.Task

class TaskMemoryDataSource {

    private val tasks = mutableListOf<Task>()

    fun getTasks(): List<Task> {
        return tasks.toList()
    }

    fun setTasks(tasks: List<Task>) {
        this.tasks.addAll(tasks)
    }

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun deleteTask(taskId: String): List<Task> {
        tasks.removeIf { it.id.toString() == taskId }
        return tasks
    }

    fun editTask(task: Task): List<Task> {
        val updatedIndex = tasks.indexOfFirst { it.id == task.id }
        tasks[updatedIndex] = task
        return tasks
    }
}
