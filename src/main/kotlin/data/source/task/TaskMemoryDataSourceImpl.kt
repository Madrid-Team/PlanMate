package data.source.task

import domain.models.task.Task

class TaskMemoryDataSourceImpl : TaskMemoryDataSource {
    private val tasks = mutableListOf<Task>()

    override fun initializeTasks(tasks: List<Task>) {
        this.tasks.addAll(tasks)
    }

    override fun getTasksFromMemory(): List<Task> {
        return tasks.toList()
    }

    override fun addTaskToMemory(task: Task) {
        tasks.add(task)
    }

    override fun removeTaskFromMemory(taskId: String): List<Task> {
        tasks.removeIf { it.id.toString() == taskId }
        return tasks
    }

    override fun editTask(task: Task): List<Task> {
        val updatedIndex = tasks.indexOfFirst { it.id == task.id }
        tasks[updatedIndex] = task
        return tasks
    }

    override fun findTaskInMemory(taskId: String): Task? {
        TODO("Not yet implemented")
    }
}