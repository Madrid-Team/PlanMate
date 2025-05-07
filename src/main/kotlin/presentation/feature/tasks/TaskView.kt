package presentation.feature.tasks


import domain.usecases.task.DisplayAllTasksUseCase
import domain.utlis.TaskExceptions
import kotlinx.coroutines.runBlocking
import presentation.components.InputReader
import presentation.components.OutputPrinter


class TaskView(
    private val displayAllTasksUseCase: DisplayAllTasksUseCase,
    private val outputPrinter: OutputPrinter,
    private val inputReader: InputReader
) {

    fun show() {
        outputPrinter.printMessage("=== Display Tasks ===")
        val projectId = inputReader.readInput("Enter project ID: ")

        try {
            val result = runBlocking { displayAllTasksUseCase.display(projectId) }
            outputPrinter.printMessage(result)
        } catch (exception: TaskExceptions) {
            outputPrinter.printMessage(exception.message.toString())
        }
    }
}