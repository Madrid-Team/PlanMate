package presentation.feature.tasks

import domain.usecases.task.DeleteTaskUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class DeleteTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskView: TaskView,
    private val deleteTaskUseCase: DeleteTaskUseCase
) {
    fun show() {
        outputPrinter.printMessage("=== Delete Task ===")
        outputPrinter.printMessage("Enter task ID to delete:")
        val taskId = inputReader.readInput()

        val success = deleteTaskUseCase.deleteTask(taskId)

        if (success) {
            outputPrinter.printMessage("Task deleted successfully.")
        } else {
            outputPrinter.printMessage("Task not found or could not be deleted.")
        }
    }
}