package presentation.feature.tasks

import domain.models.logs.CurrentUser
import domain.models.task.Task
import domain.usecases.task.EditTaskUseCase
import domain.utils.TaskExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*
import java.util.*

class EditTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val editTaskUseCase: EditTaskUseCase
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage(String.editTaskHeader)

        val projectId = inputReader.readInput(String.enterProjectId)
        val taskId = inputReader.readInput(String.enterTaskId)
        val title = inputReader.readInput(String.enterNewTitle)
        val description = inputReader.readInput(String.enterNewDescription)

        val updatedTask = Task(
            id = UUID.fromString(taskId),
            projectId = projectId,
            title = title,
            description = description,
            taskState = String.empty,
            createdBy = CurrentUser.getCurrentUser().username,
            logs = listOf()
        )

        try {
            editTaskUseCase.editTask(updatedTask)
            outputPrinter.printMessage(String.taskUpdatedSuccessfully)
        } catch (exception: TaskExceptions.TaskCannotEditException) {
            outputPrinter.printMenuItems(listOf(String.taskUpdatedUnsuccessfully, exception.message.toString()))
        }
    }
}