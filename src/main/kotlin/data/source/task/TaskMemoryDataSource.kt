package data.source.task

import domain.models.task.Task

interface TaskMemoryDataSource {
    fun initializeTasks(tasks: List<Task>)
    fun getTasksFromMemory(): List<Task>
    fun addTaskToMemory(task: Task)
    fun removeTaskFromMemory(taskId: String): List<Task>
    fun editTask(task: Task): List<Task>

    fun findTaskInMemory(taskId: String): Task?
}