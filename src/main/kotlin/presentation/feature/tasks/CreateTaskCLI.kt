package presentation.feature.tasks

import data.source.csv.user.CurrentUserProvider
import domain.models.logs.AuditLog
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.task.Task
import domain.usecases.project.GetProjectByIdUseCase
import domain.usecases.task.CreateTaskUseCase
import domain.utils.PlanMateExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*
import java.util.*

class CreateTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createTaskUseCase: CreateTaskUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val currentUserProvider: CurrentUserProvider
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage(String.createTaskHeader)
        try {
            val task = readTaskInput()
            createTaskUseCase.createTask(task)
            outputPrinter.printMessage(String.taskCreatedSuccessfully)
        } catch (exception: PlanMateExceptions) {
            outputPrinter.printMessage(exception.message.toString())
            outputPrinter.printMessage(String.taskCreatedUnsuccessfully)
        }

    }

    private suspend fun readTaskInput(): Task {
        val projectId = inputReader.readInput(String.enterProjectId)
        val title = inputReader.readInput(String.enterTaskTitle)
        val description = inputReader.readInput(String.enterTaskDescription)

        val project = getProjectByIdUseCase.getById(projectId)

        outputPrinter.printMessage(String.availableTaskStates)
        project.taskStates.forEachIndexed { index, state ->
            outputPrinter.printMessage("${index + 1}. $state")
        }

        val selectIndex = inputReader.readInput(String.selectTaskState).toIntOrNull() ?: 1
        val selectedState = project.taskStates.getOrElse(selectIndex - 1) { project.taskStates.first() }

        return Task(
            projectId = projectId,
            title = title,
            description = description,
            taskState = selectedState,
            createdBy = currentUserProvider.getCurrentUser().username,
            taskLogs = listOf(
                AuditLog(
                    operationType = OperationType.CREATE,
                    entityName = title,
                    entityType = EntityType.TASK,
                    username = currentUserProvider.getCurrentUser().username,
                ).toString()
            ),
            id = UUID.randomUUID()
        )
    }
}