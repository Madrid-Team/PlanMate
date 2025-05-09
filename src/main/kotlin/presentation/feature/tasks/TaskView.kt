package presentation.feature.tasks


import domain.usecases.task.DisplayAllTasksUseCase
import domain.utlis.ProjectExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter

class TaskView(
    private val displayAllTasksUseCase: DisplayAllTasksUseCase,
    private val outputPrinter: OutputPrinter,
    private val inputReader: InputReader
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage("=== Display Tasks ===")
        val projectId = inputReader.readInput("Enter project ID: ")

        try {
            val result = displayAllTasksUseCase.display(projectId)
            outputPrinter.printMessage(result)
        } catch (exception: ProjectExceptions) {
            outputPrinter.printMessage(exception.message.toString())
        }
    }
}