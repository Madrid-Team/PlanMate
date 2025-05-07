package presentation.feature.tasks

import domain.usecases.task.DeleteTaskUseCase
import domain.utlis.TaskExceptions
import kotlinx.coroutines.runBlocking
import presentation.components.InputReader
import presentation.components.OutputPrinter

class DeleteTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val deleteTaskUseCase: DeleteTaskUseCase
) {
    fun show() {
       runBlocking {
           outputPrinter.printMessage("=== Delete Task ===")
           outputPrinter.printMessage("Enter Project ID:")
           val projectId = inputReader.readInput()
           outputPrinter.printMessage("Enter task ID to delete:")
           val taskId = inputReader.readInput()
           try {
               deleteTaskUseCase.deleteTask(projectId,taskId)
               outputPrinter.printMessage("Task deleted successfully.")
           } catch (exception: TaskExceptions.TaskCannotDeleteException) {
               outputPrinter.printError(exception.message ?: "Task not found or could not be deleted.")
           }
       }
    }
}