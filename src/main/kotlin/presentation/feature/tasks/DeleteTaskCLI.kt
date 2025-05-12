package presentation.feature.tasks

import domain.usecases.task.DeleteTaskUseCase
import domain.utils.TaskExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class DeleteTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage(String.deleteTaskHeader)
        outputPrinter.printMessage(String.enterProjectId)
        val projectId = inputReader.readInput()
        outputPrinter.printMessage(String.enterTaskIdToDelete)
        val taskId = inputReader.readInput()
        try {
            deleteTaskUseCase.deleteTask(projectId, taskId)
            outputPrinter.printMessage(String.deleteTaskSuccess)
        } catch (exception: TaskExceptions) {
            outputPrinter.printError(String.deleteTaskException.format(exception))
        }
    }
}