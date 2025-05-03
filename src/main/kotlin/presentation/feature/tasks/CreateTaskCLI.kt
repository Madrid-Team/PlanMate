package presentation.feature.tasks

import domain.models.logs.CurrentUser
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.task.Task
import domain.usecases.logs.CreateLogUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.usecases.task.CreateTaskUseCase
import domain.utlis.ProjectNotFoundException
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class CreateTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskView: TaskView,
    private val createTaskUseCase: CreateTaskUseCase,
    private val createLogUseCase: CreateLogUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase

) {
    fun show() {
        outputPrinter.printMessage("=== Create Task ===")

        val task = readTaskInput()

        val result = createTaskUseCase.createTask(task)

        printResult(result)
    }

    private fun readTaskInput(): Task {
        val projectId = inputReader.readInput("Enter project ID: \n")
        val title = inputReader.readInput("Enter task title: ")
        val description = inputReader.readInput("Enter task description: ")
        val result = getProjectByIdUseCase.invoke(projectId)

        if (result.isFailure) {
            throw ProjectNotFoundException()
        }
        val project = result.getOrThrow()

        outputPrinter.printMessage("Available task states:")
        project.taskStates.forEachIndexed { index, state ->
            outputPrinter.printMessage("${index + 1}. $state")
        }

        val selectIndex = inputReader.readInput("Select task state: ").toIntOrNull() ?: 1
        val selectedState = project.taskStates.getOrElse(selectIndex - 1) { project.taskStates.first() }

        return Task(
            projectId = projectId,
            title = title,
            description = description,
            taskState = selectedState,
            createdBy = CurrentUser.getCurrentUser()?.username?: "Unknown",
            logs =  listOf(
                createLogUseCase.invoke(
                    operationType = OperationType.CREATE,
                    entityName = title,
                    entityType = EntityType.TASK,
                    username = CurrentUser.getCurrentUser()!!.username,
                )
            ),
            id = UUID.randomUUID()
        )
    }

    private fun printResult(result: Boolean) {
        if (result) {
            outputPrinter.printMessage("Task created successfully")
        } else {
            outputPrinter.printMessage("Failed to create task")
        }
    }

}