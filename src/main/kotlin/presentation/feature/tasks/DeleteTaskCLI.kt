package presentation.feature.tasks

import domain.usecases.task.DeleteTaskUseCase
import domain.utlis.TaskExceptions
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
        try {
            val taskId = inputReader.readInput()
            deleteTaskUseCase.deleteTask(taskId)
            outputPrinter.printMessage("Task deleted successfully.")
        } catch (exception: TaskExceptions.TaskCannotDeleteException) {
            outputPrinter.printError(exception.message ?: "Task not found or could not be deleted.")
        }
    }
}