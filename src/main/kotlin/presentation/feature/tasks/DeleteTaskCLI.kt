package presentation.feature.tasks

import domain.usecases.task.DeleteTaskUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class DeleteTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val deleteTaskUseCase: DeleteTaskUseCase
) {
    fun show() {
        outputPrinter.printMessage("=== Delete Task ===")
        outputPrinter.printMessage("Enter task ID to delete:")
        val taskId = inputReader.readInput()

        val result = deleteTaskUseCase.deleteTask(taskId)

        if (result.isSuccess) {
            outputPrinter.printMessage("Task deleted successfully.")
        } else {
            outputPrinter.printError("Task not found or could not be deleted.")
        }
    }
}