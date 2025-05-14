package presentation.feature.tasks

import domain.models.task.Task
import domain.usecases.task.GetTasksByProjectIdUseCase
import domain.utils.TaskExceptions
import presentation.components.InputReader
import presentation.components.OutputPrinter

class TaskViewer(
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,

    ) {
    suspend fun displayAllTasks(projectId: String) {
        try {

            val allTasks = getTasksByProjectIdUseCase.getTaskByProjectId(projectId)

            printTasksByState(allTasks)
        } catch (e: TaskExceptions) {
            outputPrinter.printError(e.message.toString())
        }

    }

    fun printTasksByState(tasks: List<Task>) {
        val tasksByState = tasks.groupBy { it.taskState }

        tasksByState.forEach { (state, tasksInState) ->
            outputPrinter.printMessage("Tasks in state: $state (${tasksInState.size} tasks)")
            outputPrinter.printMessage("------------------------------")

            tasksInState.forEach { task ->
                outputPrinter.printMessage("ID: ${task.id}")
                outputPrinter.printMessage("Title: ${task.title}")
                outputPrinter.printMessage("Description: ${task.description}")
                outputPrinter.printMessage("Created by: ${task.createdBy}")
                outputPrinter.printMessage("Logs: ${task.taskLogs.size} entries")
                outputPrinter.printMessage("------------------------------")
            }
            outputPrinter.printMessage("\n")
        }
    }

}