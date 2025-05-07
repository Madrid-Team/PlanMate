package presentation.feature.tasks

import domain.models.logs.CurrentUser
import domain.models.task.Task
import domain.usecases.task.EditTaskUseCase
import domain.utlis.PlanMateExceptions
import domain.utlis.TaskExceptions
import kotlinx.coroutines.runBlocking
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class EditTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskView: TaskView,
    private val editTaskUseCase: EditTaskUseCase
) {
    fun show() {
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
            createdBy = CurrentUser.getCurrentUser()?.username ?: "UNKNOWN",
            logs = listOf()
        )

        try {
            runBlocking {
                editTaskUseCase.editTask(updatedTask)
                outputPrinter.printMessage("Task updated successfully")
            }
        } catch (exception: TaskExceptions.TaskCannotEditException) {
            outputPrinter.printError(exception.message ?: "Failed to update task")
        }
    }
}