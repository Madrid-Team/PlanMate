package presentation.feature.tasks


import domain.usecases.task.DisplayAllTasksUseCase
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

        val result = displayAllTasksUseCase.display(projectId)

        result
            .onSuccess { outputPrinter.printMessage(it) }
            .onFailure { outputPrinter.printMessage("Error: ${it.message}") }
    }

}