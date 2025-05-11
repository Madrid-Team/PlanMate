package presentation.feature.tasks

import domain.models.logs.CurrentUser
import domain.models.task.Task
import domain.usecases.task.EditTaskUseCase
import domain.utils.TaskExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class EditTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val editTaskUseCase: EditTaskUseCase
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage("=== Edit Task ===")

        val projectId = inputReader.readInput("Enter project ID: ")
        val taskId = inputReader.readInput("Enter task ID: ")
        val title = inputReader.readInput("Enter new title: ")
        val description = inputReader.readInput("Enter new description: ")

        val updatedTask = Task(
            id = UUID.fromString(taskId),
            projectId = projectId,
            title = title,
            description = description,
            taskState = "",
            createdBy = CurrentUser.getCurrentUser().username,
            logs = listOf()
        )

        try {
            editTaskUseCase(updatedTask)
            outputPrinter.printMessage("Task updated successfully")
        } catch (exception: TaskExceptions.TaskCannotEditException) {
            outputPrinter.printError(exception.message ?: "Failed to update task")
        }
    }
}